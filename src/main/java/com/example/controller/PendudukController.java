package com.example.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;
import com.example.service.SidukService;

@Controller
public class PendudukController {
	@Autowired
	SidukService sidukDAO;
	
	@RequestMapping("/")
	public String index() {
		return "penduduk-search";
	}
	
	@InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	@RequestMapping("/penduduk")
	public String viewNIK(@RequestParam(value = "nik", required = false) String nik, Model model) {
		PendudukModel penduduk = sidukDAO.selectPenduduk(nik);
		
		if (penduduk == null) {
			model.addAttribute("error", "Penduduk tidak ditemukan");
			
			return "custom-error";
		}
		else {
			model.addAttribute("nomor", nik);
			model.addAttribute("penduduk", penduduk);
			
			return "penduduk-view-nik";
		}
	}
	
	@RequestMapping("/penduduk/tambah")
	public String pendudukTambah(PendudukModel penduduk) {
		return "penduduk-add";
	}
	
	private String nik(Date tanggal_lahir, Boolean isFemale, KeluargaModel keluarga) {
		// kecamatan code
		String kecamatan = keluarga.getKelurahan().getKecamatan().getKode_kecamatan().substring(0,6);
		// birth date
		Integer month = tanggal_lahir.getMonth()+1;
		Integer date = tanggal_lahir.getDate();
		Integer year = tanggal_lahir.getYear() % 100;
		
		if (isFemale) { 
			date += 40; 
		}
		
		String partNIK = String.format("%s%02d%02d%02d%%", kecamatan, date, month, year);
		
		// get similar penduduk
		PendudukModel highestSimilarPenduduk = sidukDAO.selectHighestNIK(partNIK);
		Integer nomor;
		if (highestSimilarPenduduk != null) {
			String highestNIK = highestSimilarPenduduk.getNik();
			String unique = highestNIK.substring(highestNIK.length() - 4, highestNIK.length());
			nomor = Integer.parseInt(unique) + 1;
		}
		else {
			nomor = 1;
		}
		
		String nik = String.format("%s%02d%02d%02d%04d", kecamatan, date, month, year, nomor);
		
		return nik;
		
	}
	
	@RequestMapping(value = "/penduduk/tambah", method = RequestMethod.POST)
	public String pendudukTambah(Model model, @Valid PendudukModel penduduk, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return "penduduk-add";
		}
		else {
			KeluargaModel keluarga = sidukDAO.selectKeluarga(penduduk.getId_keluarga());
			
			// if adding a person who is alive, activate the card again
			if(!penduduk.getIs_wafat()) {
				keluarga.setIs_tidak_berlaku(false);
				sidukDAO.updateKeluarga(keluarga);
			}
			
			String nik = this.nik(penduduk.getTanggal_lahir(), penduduk.getJenis_kelamin(), keluarga);
			penduduk.setNik(nik);
			
			sidukDAO.addPenduduk(penduduk);
			model.addAttribute("nik", nik);
			
			return "penduduk-success";
		}
	}
	
	@RequestMapping(value = "/penduduk/mati", method = RequestMethod.POST)
	public String pendudukMati(@RequestParam(value = "nik", required = true) String nik,
			Model model) {
		
		// update penduduk
		PendudukModel penduduk = sidukDAO.selectPenduduk(nik);
		penduduk.setIs_wafat(true);
		sidukDAO.updatePenduduk(penduduk);
		
		// check if card is valid
		List<PendudukModel> anggota = sidukDAO.selectSemuaPenduduk(penduduk.getId_keluarga());
		Boolean tidakBerlaku = true;
		for (int i = 0; tidakBerlaku == true && i < anggota.size(); i++) {
			if (!anggota.get(i).getIs_wafat()) {
				tidakBerlaku = false;
			}
		}
		if (tidakBerlaku == true) {
			KeluargaModel keluarga = sidukDAO.selectKeluarga(penduduk.getId_keluarga());
			keluarga.setIs_tidak_berlaku(true);
			sidukDAO.updateKeluarga(keluarga);
		}
		model.addAttribute("nik", nik);
		
		return "penduduk-nonaktif-success";
	}
	
	@RequestMapping("/penduduk/ubah/{nik}")
	public String pendudukUbah(Model model, @PathVariable(value = "nik") String nik) {
		PendudukModel penduduk = sidukDAO.selectPenduduk(nik);
		model.addAttribute("pendudukModel", penduduk);
		return "penduduk-update";
	}
	
	@RequestMapping(value = "/penduduk/ubah/{nik}", method = RequestMethod.POST)
	public String pendudukUbahSubmit(Model model, @PathVariable(value = "nik") String nik, @Valid PendudukModel penduduk, BindingResult bindingResult) {
		PendudukModel p = sidukDAO.selectPenduduk(nik);
		penduduk.setId(p.getId());
		penduduk.setNik(nik);
		if(bindingResult.hasErrors()) {
			return "penduduk-update";
		}
		else {
			if (p.getJenis_kelamin() != penduduk.getJenis_kelamin() || !p.getTanggal_lahir().equals(penduduk.getTanggal_lahir())) {
				KeluargaModel keluarga = sidukDAO.selectKeluarga(penduduk.getId_keluarga());
				String newNik = this.nik(penduduk.getTanggal_lahir(), penduduk.getJenis_kelamin(), keluarga);
				penduduk.setNik(newNik);
			}
			model.addAttribute("newNik", penduduk.getNik());
			sidukDAO.updatePenduduk(penduduk);
			return "penduduk-update-success";
		}
	}
	
	@RequestMapping("/penduduk/cari")
	public String cariPenduduk(@RequestParam(value = "kt", required = false) Integer kt, 
			@RequestParam(value = "kc", required = false) Integer kc,
			@RequestParam(value = "kl", required = false) Integer kl,
			Model model) {
		
		if (kl == null) {
			List<KotaModel> semuaKota = sidukDAO.selectSemuaKota();
			List<KecamatanModel> semuaKecamatan = sidukDAO.selectSemuaKecamatan();
			List<KelurahanModel> semuaKelurahan = sidukDAO.selectSemuaKelurahan();
		
			model.addAttribute("kotas", semuaKota);
			model.addAttribute("kecamatans", semuaKecamatan);
			model.addAttribute("kelurahans", semuaKelurahan);
		}
		
		if (kl != null) {
			KelurahanModel kelurahan = sidukDAO.selectKelurahan(kl);
			
			model.addAttribute("skota", kelurahan.getKecamatan().getKota().getNama_kota());
			model.addAttribute("skecamatan", kelurahan.getKecamatan().getNama_kecamatan());
			model.addAttribute("skelurahan", kelurahan.getNama_kelurahan());
			
			List<PendudukModel> penduduks = sidukDAO.selectPendudukDalamKelurahan(kl);
			
			if (penduduks.size() == 0) {
				model.addAttribute("error", "Tidak ada penduduk di wilayah ini");
				return "custom-error";
			}
			else {
				// find youngest and oldest
				PendudukModel termuda = null;
				PendudukModel tertua = null;
				for (int i = 0; i < penduduks.size(); i++) {
					if (termuda == null || penduduks.get(i).getTanggal_lahir().after(termuda.getTanggal_lahir())) {
						termuda = penduduks.get(i);
					}
					
					if (tertua == null || penduduks.get(i).getTanggal_lahir().before(tertua.getTanggal_lahir())) {
						tertua = penduduks.get(i);
					}
				}
				
				model.addAttribute("termuda", termuda);
				model.addAttribute("tertua", tertua);
				model.addAttribute("penduduks", penduduks);
				
				return "penduduk-view-kota";
			}
		}
		else if (kc != null) {
			KecamatanModel kecamatan = sidukDAO.selectKecamatan(kc);
			
			model.addAttribute("skota", kecamatan.getKota());
			model.addAttribute("skecamatan", kecamatan);
			
			return "penduduk-search-kota";
		}
		else if (kt != null) {
			KotaModel kota = sidukDAO.selectKota(kt);
			model.addAttribute("skota", kota);
			
			return "penduduk-search-kota";
		}
		else {
			return "penduduk-search-kota";
		}
	}
	
}
