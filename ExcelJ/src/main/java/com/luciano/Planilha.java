package com.luciano;

import java.math.BigDecimal;
import java.util.Objects;

public class Planilha {
	
	private int codigo;
	private String nome;
	private String marca;
	private BigDecimal preco;
	
	public Planilha(int codigo, String nome, String marca, BigDecimal preco) {
		super();
		this.codigo = codigo;
		this.nome = nome;
		this.marca = marca;
		this.preco = preco;
	}
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, marca, nome, preco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Planilha other = (Planilha) obj;
		return codigo == other.codigo && Objects.equals(marca, other.marca) && Objects.equals(nome, other.nome)
				&& Objects.equals(preco, other.preco);
	}
	
	
	
	
	
}
