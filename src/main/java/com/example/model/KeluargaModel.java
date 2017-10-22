package com.example.model;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaModel {
	private Integer id;
	private String nomor_kk;
	
	@NotNull(message="Silakan isi")
	@Size(min=1, message="Silakan isi")
	private String alamat;
	
	@NotNull(message="Silakan isi")
	@Size(min=1, message="Silakan isi")
	@Digits(integer=6, fraction=0, message="Harus sesuai dengan format RT (contoh: 05)")
	private String rt;
	
	@NotNull(message="Silakan isi")
	@Size(min=1, message="Silakan isi")
	@Digits(integer=6, fraction=0, message="Harus sesuai dengan format RW (contoh: 05)")
	private String rw;
	
	private Integer id_kelurahan;
	private Boolean is_tidak_berlaku;
	
	private List<PendudukModel> penduduk;
	private KelurahanModel kelurahan;
}