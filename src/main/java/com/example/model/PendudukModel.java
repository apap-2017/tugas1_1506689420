package com.example.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendudukModel {
	private Integer id;
	
	private String nik;
	
	@Size(min=2, max=128)
	@Pattern(regexp="^[A-Za-z\\s]+$",message="Harus sesuai dengan format nama")
	private String nama;
	
	@NotNull(message="Silakan isi")
	private String tempat_lahir;
	
	@NotNull(message="Silakan isi")
	@Past
	private Date tanggal_lahir;
	
	@NotNull
	private Boolean jenis_kelamin;
	
	@NotNull(message="Silakan isi")
	private Boolean is_wni;
	
	@NotNull(message="Silakan isi")
	private Integer id_keluarga;
	
	@NotNull(message="Silakan isi")
	@Size(min=2, max=64)
	private String agama;
	
	@NotNull(message="Silakan isi")
	@Size(min=2, max=64)
	private String pekerjaan;
	
	@NotNull(message="Silakan isi")
	@Size(min=2, max=64)
	private String status_perkawinan;
	
	@NotNull(message="Silakan isi")
	@Size(min=2, max=64)
	private String status_dalam_keluarga;
	
	@NotNull(message="Silakan isi")
	private String golongan_darah;
	
	@NotNull(message="Silakan isi")
	private Boolean is_wafat;
	
	private KeluargaModel keluarga;
}