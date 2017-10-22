package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;

@Mapper
public interface SidukMapper {
	
	@Select("SELECT * FROM penduduk WHERE nik=#{nik}")
	@Results(value = { @Result(property = "nik", column = "nik"), 
			@Result(property = "nama", column = "nama"),
			@Result(property = "tempat_lahir", column = "tempat_lahir"),
			@Result(property = "jenis_kelamin", column = "jenis_kelamin"),
			@Result(property = "is_wni", column = "is_wni"),
			@Result(property = "id_keluarga", column = "id_keluarga"),
			@Result(property = "agama", column = "agama"),
			@Result(property = "pekerjaan", column = "pekerjaan"),
			@Result(property = "status_perkawinan", column = "status_perkawinan"),
			@Result(property = "status_dalam_keluarga", column = "status_dalam_keluarga"),
			@Result(property = "golongan_darah", column = "golongan_darah"),
			@Result(property = "is_wafat", column = "is_wafat"),
			@Result(property = "keluarga", column = "id_keluarga", 
			javaType = KeluargaModel.class,
			many = @Many(select = "selectKeluarga")) 
	})
	PendudukModel selectPenduduk(@Param("nik") String nik);
	
	@Select("SELECT * FROM penduduk WHERE nik LIKE #{partNIK} ORDER BY nik DESC LIMIT 1")
	PendudukModel selectHighestNIK(@Param("partNIK") String partNIK);
	
	@Select("SELECT * FROM penduduk,keluarga WHERE penduduk.id_keluarga=keluarga.id AND keluarga.id_kelurahan=#{kelurahan}")
	List<PendudukModel> selectPendudukDalamKelurahan(@Param("kelurahan") Integer kelurahan);
	
	
	@Insert("INSERT INTO penduduk (nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, is_wni, "
			+ "id_keluarga, agama, pekerjaan, status_perkawinan, status_dalam_keluarga, golongan_darah, is_wafat) "
			+ "VALUES (#{nik}, #{nama}, #{tempat_lahir}, #{tanggal_lahir}, #{jenis_kelamin}, #{is_wni}, "
			+ "#{id_keluarga}, #{agama}, #{pekerjaan}, #{status_perkawinan}, #{status_dalam_keluarga}, #{golongan_darah}, #{is_wafat})")
	void addPenduduk(PendudukModel penduduk);
	
	@Update("UPDATE penduduk SET nik=#{nik}, nama=#{nama}, tempat_lahir=#{tempat_lahir}, tanggal_lahir=#{tanggal_lahir}, jenis_kelamin=#{jenis_kelamin}, "
			+ "is_wni=#{is_wni}, id_keluarga=#{id_keluarga}, agama=#{agama}, pekerjaan=#{pekerjaan}, status_perkawinan = #{status_perkawinan}, "
			+ "status_dalam_keluarga = #{status_dalam_keluarga}, golongan_darah = #{golongan_darah}, is_wafat = #{is_wafat} "
			+ "WHERE id=#{id}")
	void updatePenduduk(PendudukModel penduduk);
	
	
	@Select("SELECT * FROM keluarga WHERE keluarga.id=#{id_keluarga}")
	@Results(value = { @Result(property = "id", column = "id"), 
			@Result(property = "nomor_kk", column = "nomor_kk"),
			@Result(property = "alamat", column = "alamat"),
			@Result(property = "rt", column = "rt"),
			@Result(property = "rw", column = "rw"),
			@Result(property = "id_kelurahan", column = "id_kelurahan"),
			@Result(property = "is_tidak_berlaku", column = "is_tidak_berlaku"),
			@Result(property = "kelurahan", column = "id_kelurahan", 
			javaType = KelurahanModel.class,
			many = @Many(select = "selectKelurahan")) 
	})
	KeluargaModel selectKeluarga(@Param("id_keluarga") Integer id_keluarga);
	
	@Select("SELECT * FROM keluarga WHERE keluarga.nomor_kk=#{nkk} LIMIT 1")
	@Results(value = { 
			@Result(property = "id", column = "id"), 
			@Result(property = "nomor_kk", column = "nomor_kk"),
			@Result(property = "alamat", column = "alamat"),
			@Result(property = "rt", column = "rt"),
			@Result(property = "rw", column = "rw"),
			@Result(property = "id_kelurahan", column = "id_kelurahan"),
			@Result(property = "is_tidak_berlaku", column = "is_tidak_berlaku"),
			@Result(property = "kelurahan", column = "id_kelurahan", 
			javaType = KelurahanModel.class,
			many = @Many(select = "selectKelurahan")),
			@Result(property = "penduduk", column = "id",
			javaType = List.class,
			many = @Many(select = "selectSemuaPenduduk"))
	})
	KeluargaModel selectKartuKeluarga(@Param("nkk") String nkk);
	
	@Select("SELECT * FROM keluarga WHERE nomor_kk LIKE #{partNKK} ORDER BY nomor_kk DESC LIMIT 1")
	KeluargaModel selectHighestNKK(@Param("partNKK") String partNKK);
	
	@Insert("INSERT INTO keluarga (nomor_kk, alamat, rt, rw, id_kelurahan) "
			+ "VALUES (#{nomor_kk}, #{alamat}, #{rt}, #{rw}, #{id_kelurahan})")
	void addKeluarga(KeluargaModel keluarga);
	
	@Insert("UPDATE keluarga SET nomor_kk=#{nomor_kk}, alamat=#{alamat}, rt=#{rt}, rw=#{rw}, id_kelurahan=#{id_kelurahan}, is_tidak_berlaku=#{is_tidak_berlaku} "
			+ "WHERE nomor_kk=#{nomor_kk}")
	void updateKeluarga(KeluargaModel keluarga);
	
	@Select("SELECT * FROM kelurahan")
	List<KelurahanModel> selectSemuaKelurahan();
	
	@Select("SELECT * FROM kelurahan WHERE kelurahan.id=#{id_kelurahan}")	
	@Results(value = { @Result(property = "id", column = "id"), 
			@Result(property = "kode_kelurahan", column = "kode_kelurahan"),
			@Result(property = "id_kecamatan", column = "id_kecamatan"),
			@Result(property = "nama_kelurahan", column = "nama_kelurahan"),
			@Result(property = "kode_pos", column = "kode_pos"),
			@Result(property = "kecamatan", column = "id_kecamatan", 
			javaType = KecamatanModel.class,
			many = @Many(select = "selectKecamatan")) 
	})
	KelurahanModel selectKelurahan(@Param("id_kelurahan") Integer id_kelurahan);
	
	
	@Select("SELECT * FROM kecamatan")
	List<KecamatanModel> selectSemuaKecamatan();
	
	@Select("SELECT * FROM kecamatan WHERE kecamatan.id=#{id_kecamatan}")
	@Results(value = { @Result(property = "id", column = "id"), 
			@Result(property = "kode_kecamatan", column = "kode_kecamatan"),
			@Result(property = "id_kota", column = "id_kota"),
			@Result(property = "nama_kecamatan", column = "nama_kecamatan"),
			@Result(property = "kota", column = "id_kota", 
			javaType = KotaModel.class,
			many = @Many(select = "selectKota")) 
	})
	KecamatanModel selectKecamatan(@Param("id_kecamatan") Integer id_kecamatan);
	
	@Select("SELECT * FROM kota")
	List<KotaModel> selectSemuaKota();
	
	@Select("SELECT * FROM kota WHERE kota.id=#{id_kota}")
	KotaModel selectKota(@Param("id_kota") Integer id_kota);
	

	@Select("SELECT * FROM penduduk WHERE id_keluarga=#{id_keluarga}")
	List<PendudukModel> selectSemuaPenduduk(@Param("id_keluarga") Integer id_keluarga);
}