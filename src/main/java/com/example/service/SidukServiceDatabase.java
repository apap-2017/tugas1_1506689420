package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.SidukMapper;
import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;

@Service
public class SidukServiceDatabase implements SidukService {
	@Autowired
	private SidukMapper sidukMapper;

	@Override
	public PendudukModel selectPenduduk(String nik) {
		return sidukMapper.selectPenduduk(nik);
	}
	
	@Override
	public PendudukModel selectHighestNIK(String partNIK) {
		
		return sidukMapper.selectHighestNIK(partNIK);
	}
	
	@Override
	public List<PendudukModel> selectPendudukDalamKelurahan(Integer kelurahan) {
		
		return sidukMapper.selectPendudukDalamKelurahan(kelurahan);
	}

	@Override
	public void addPenduduk(PendudukModel penduduk) {
		
		sidukMapper.addPenduduk(penduduk);
	}

	@Override
	public void updatePenduduk(PendudukModel penduduk) {
		
		sidukMapper.updatePenduduk(penduduk);
	}

	//@Override
	//public List<KeluargaModel> selectSemuaKeluarga() {
		
		//return sidukMapper.selectSemuaKeluarga();
	//}

	@Override
	public KeluargaModel selectKeluarga(Integer id_keluarga) {
		
		return sidukMapper.selectKeluarga(id_keluarga);
	}

	@Override
	public KeluargaModel selectKartuKeluarga(String nkk) {
		
		return sidukMapper.selectKartuKeluarga(nkk);
	}

	@Override
	public KeluargaModel selectHighestNKK(String partNKK) {
		
		return sidukMapper.selectHighestNKK(partNKK);
	}

	@Override
	public void addKeluarga(KeluargaModel keluarga) {
		
		sidukMapper.addKeluarga(keluarga);
	}
	
	@Override
	public void updateKeluarga(KeluargaModel keluarga) {
		
		sidukMapper.updateKeluarga(keluarga);
	}

	@Override
	public List<KelurahanModel> selectSemuaKelurahan() {
		
		return sidukMapper.selectSemuaKelurahan();
	}

	@Override
	public KelurahanModel selectKelurahan(Integer id_kelurahan) {
		
		return sidukMapper.selectKelurahan(id_kelurahan);
	}

	@Override
	public List<KecamatanModel> selectSemuaKecamatan() {
		
		return sidukMapper.selectSemuaKecamatan();
	}

	@Override
	public KecamatanModel selectKecamatan(Integer id_kecamatan) {
		
		return sidukMapper.selectKecamatan(id_kecamatan);
	}

	@Override
	public List<KotaModel> selectSemuaKota() {
		
		return sidukMapper.selectSemuaKota();
	}

	@Override
	public KotaModel selectKota(Integer id_kota) {
		
		return sidukMapper.selectKota(id_kota);
	}

	@Override
	public List<PendudukModel> selectSemuaPenduduk(Integer id_keluarga) {
		
		return sidukMapper.selectSemuaPenduduk(id_keluarga);
	}

}
