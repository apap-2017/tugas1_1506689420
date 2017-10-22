package com.example.service;

import java.util.List;

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel; 

public interface SidukService {
	
	PendudukModel selectPenduduk(String nik);
	PendudukModel selectHighestNIK(String partNIK);
	List<PendudukModel> selectPendudukDalamKelurahan(Integer kelurahan);
	void addPenduduk(PendudukModel penduduk);
	void updatePenduduk(PendudukModel penduduk);
	
	KeluargaModel selectKeluarga(Integer id_keluarga);
	KeluargaModel selectKartuKeluarga(String nkk);
	KeluargaModel selectHighestNKK(String partNKK);
	void addKeluarga(KeluargaModel keluarga);
	void updateKeluarga(KeluargaModel keluarga);
	
	List<KelurahanModel> selectSemuaKelurahan();
	KelurahanModel selectKelurahan(Integer id_kelurahan);
	
	List<KecamatanModel> selectSemuaKecamatan();
	KecamatanModel selectKecamatan(Integer id_kecamatan);
	
	List<KotaModel> selectSemuaKota();
	KotaModel selectKota(Integer id_kota);
	
	List<PendudukModel> selectSemuaPenduduk(Integer id_keluarga);	
}