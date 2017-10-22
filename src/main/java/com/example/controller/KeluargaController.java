package com.example.controller;

import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class KeluargaController {
	@Autowired
	SidukService sidukDAO;
	
	@RequestMapping("/keluarga")
	public String viewNKK(@RequestParam(value = "nkk", required = false) String nkk, Model model) {
		KeluargaModel keluarga = sidukDAO.selectKartuKeluarga(nkk);
		
		if (keluarga == null) {
			model.addAttribute("error", "Keluarga tidak ditemukan");
			
			return "custom-error";
		}
		else {
			model.addAttribute("nomor", nkk);
			model.addAttribute("keluarga", keluarga);
			
			return "keluarga-view";
		}
	}
	
	private String nkk(KelurahanModel kelurahan) {
		String nkk = "";
		
		// kecamatan code
		String kecamatan = kelurahan.getKecamatan().getKode_kecamatan().substring(0,6);
		// current date
		Calendar now = Calendar.getInstance();
		String year = Integer.toString(now.get(Calendar.YEAR)).substring(2);
		
		
		String partNKK = String.format("%s%d%02d%s%%", kecamatan, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH)+1, year);
		
		// get similar penduduk
		KeluargaModel highestSimilarKeluarga = sidukDAO.selectHighestNKK(partNKK);
		Integer nomor;
		if (highestSimilarKeluarga != null) {
			String highestNKK = highestSimilarKeluarga.getNomor_kk();
			String unique = highestNKK.substring(highestNKK.length() - 4);
			nomor = Integer.parseInt(unique) + 1;
		}
		else {
			nomor = 1;
		}
		
		nkk = String.format("%s%d%02d%s%04d", kecamatan, now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH), year, nomor);
		
		return nkk;
		
	}
	
	@RequestMapping("/keluarga/tambah")
	public String keluargaTambah(Model model, KeluargaModel keluarga) {
		List<KotaModel> semuaKota = sidukDAO.selectSemuaKota();
		List<KecamatanModel> semuaKecamatan = sidukDAO.selectSemuaKecamatan();
		List<KelurahanModel> semuaKelurahan = sidukDAO.selectSemuaKelurahan();
		
		model.addAttribute("kotas", semuaKota);
		model.addAttribute("kecamatans", semuaKecamatan);
		model.addAttribute("kelurahans", semuaKelurahan);
		
		return "keluarga-add";
	}
	
	@RequestMapping(value = "/keluarga/tambah", method = RequestMethod.POST)
	public String keluargaTambah(Model model, @Valid KeluargaModel keluarga, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "keluarga-add";
		}
		else {
			KelurahanModel kelurahanInstance = sidukDAO.selectKelurahan(keluarga.getId_kelurahan());
			
			String nkk = this.nkk(kelurahanInstance);
			keluarga.setNomor_kk(nkk);
			
			sidukDAO.addKeluarga(keluarga);
			model.addAttribute("nkk", nkk);
			
			return "keluarga-success";
		}
	}
	
	@RequestMapping("/keluarga/ubah/{nkk}")
	public String keluargaUbah(Model model, @PathVariable(value = "nkk") String nkk) {
		KeluargaModel keluarga = sidukDAO.selectKartuKeluarga(nkk);
		
		List<KotaModel> semuaKota = sidukDAO.selectSemuaKota();
		List<KecamatanModel> semuaKecamatan = sidukDAO.selectSemuaKecamatan();
		List<KelurahanModel> semuaKelurahan = sidukDAO.selectSemuaKelurahan();
		
		model.addAttribute("keluarga", keluarga);
		model.addAttribute("kotas", semuaKota);
		model.addAttribute("kecamatans", semuaKecamatan);
		model.addAttribute("kelurahans", semuaKelurahan);
		
		return "keluarga-update";
	}
	
	@RequestMapping(value = "/keluarga/ubah/{nkk}", method = RequestMethod.POST)
	public String keluargaUbahSubmit(Model model, @PathVariable(value = "nkk") String nkk, @Valid KeluargaModel keluarga, BindingResult bindingResult) {
		KeluargaModel k = sidukDAO.selectKartuKeluarga(nkk);
		
		if (k.getId_kelurahan() != keluarga.getId_kelurahan()) {
			KelurahanModel kelurahan = sidukDAO.selectKelurahan(keluarga.getId_kelurahan());
			String newNkk = this.nkk(kelurahan);
			keluarga.setNomor_kk(newNkk);
		}
		
		model.addAttribute("newNkk", keluarga.getNomor_kk());
		sidukDAO.updateKeluarga(keluarga);
		
		return "keluarga-update-success";
	}
}
