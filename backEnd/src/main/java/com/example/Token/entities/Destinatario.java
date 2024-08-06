package com.example.Token.entities;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Destinatario {
	@CsvBindByName(column = "Ragione Sociale")
private String ragioneSociale;
	
	@CsvBindByName(column = "Nome")
private String nome;
	
	@CsvBindByName(column = "Cognome")
private String cognome;
	
	@CsvBindByName(column = "CAP")
private String cap;
	
	@CsvBindByName(column = "Citta")
private String città;

	@CsvBindByName(column = "Provincia")
	private String provincia;
	
	@CsvBindByName(column = "Stato")
private String stato;
	
	@CsvBindByName(column = "Indirizzo")
private String indirizzo;
	
	@CsvBindByName(column = "CompletamentoIndirizzo")
private String completamentoIndirizzo;
	
	@CsvBindByName(column = "CompletamentoNominativo")
private String completamentoNominativo;
	
	@CsvBindByName(column = "Nome File")
private String nomeFile;
	
	@CsvBindByName(column = "Codice Fiscale")
private String codiceFiscale;
	
	@CsvBindByName(column = "Telefono")
private String telefono;
}
