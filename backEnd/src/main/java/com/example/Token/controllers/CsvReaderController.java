package com.example.Token.controllers;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.Token.entities.Destinatario;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CsvReaderController{
	
	public HashMap<Integer,Destinatario> csvToList(MultipartFile file) throws IOException, CsvException {
		HashMap<Integer, Destinatario> destinatarioMap = new HashMap<Integer, Destinatario>();
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        try (Reader reader = new InputStreamReader(file.getInputStream());
                CSVReader csvReader = new CSVReaderBuilder(reader)
                        .withCSVParser(parser)
                        .build()) {
            CsvToBean<Destinatario> csvToBean = new CsvToBeanBuilder<Destinatario>(csvReader)
                    .withType(Destinatario.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Destinatario> destinatarioList = csvToBean.parse();
            int n=1;
            for (Destinatario d: destinatarioList) {
            	destinatarioMap.put(n, d);
            	n++;
            }
            return destinatarioMap;
        }
    }
	
	public MultipartFile replaceCsvHeader(MultipartFile file, List<String> newHeader) throws IOException, CsvValidationException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
        StringWriter writer = new StringWriter();
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(parser)
                     .build();
             CSVWriter csvWriter = new CSVWriter(writer,
                     ';',
                     CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {
            csvWriter.writeNext(newHeader.toArray(new String[0]));
            csvReader.readNext();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                csvWriter.writeNext(nextLine);
            }
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(writer.toString().getBytes());
        return new MockMultipartFile("file", file.getOriginalFilename(), "text/csv", byteArrayInputStream);
    }

	
	
	
	public String[] getCsvHeader (MultipartFile file) throws IOException, CsvException{
		//creo parser ovvero comm
		CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
        //
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(parser)
                     .build()) {
            String[] firstLine = csvReader.readNext(); 
                return firstLine;
                   }
    }

		public ArrayList<String> csvHeaderNormalizer (String[] firstLine) {
			ArrayList<String> newFirstLine = new ArrayList<String>();
			ArrayList<String> notRecognizedArray = new ArrayList<String>();
		    //notRecognizedArray.add("");
		for (String head: firstLine) {
			int n = 0;
			if (head.trim().equalsIgnoreCase("nominativo")||head.trim().equalsIgnoreCase("contribuente")||head.trim().equalsIgnoreCase("ragione sociale")||head.trim().equalsIgnoreCase("ragionesociale")) {
				newFirstLine.add("Ragione Sociale");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("nome")){
				newFirstLine.add("Nome");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("cognome")){
				newFirstLine.add("Cognome");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("cap")){
				newFirstLine.add("CAP");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("città")||head.trim().equalsIgnoreCase("citta")){
				newFirstLine.add("Citta");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("provincia")){
				newFirstLine.add("Provincia");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("stato")){
				newFirstLine.add("Stato");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("indirizzo")){
				newFirstLine.add("Indirizzo");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("completamentoindirizzo")){
				newFirstLine.add("CompletamentoIndirizzo");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("completamentonominativo")){
				newFirstLine.add("CompletamentoNominativo");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("nome file")||head.trim().equalsIgnoreCase("nomefile")){
				newFirstLine.add("Nome File");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("codice fiscale")||head.trim().equalsIgnoreCase("codicefiscale")){
				newFirstLine.add("Codice Fiscale");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("telefono")){
				newFirstLine.add("Telefono");
				n = 1;
			}
			if (head.trim().equalsIgnoreCase("elimina colonna")){
				newFirstLine.add("elimina colonna");
				n = 1;
			}
			if (n!=1) {
			notRecognizedArray.add(head);
		}}
		if (notRecognizedArray.size()!=1) {
			return notRecognizedArray;
		}
		return newFirstLine;
}
		
		public ArrayList<String> csvHeaderCorrection (HashMap<String, String> params, String[] firstLine) {
			ArrayList<String> newFirstLine = new ArrayList<String>();
			ArrayList<String> notRecognizedArray = new ArrayList<String>();
			notRecognizedArray.add("Header non riconosciuti: ");
			for (String actualKey: firstLine) {
				boolean done = false;
				for (Map.Entry<String, String> wrongKey: params.entrySet()) {
				if (actualKey.equals(wrongKey.getKey())) {
					newFirstLine.add(wrongKey.getValue());	
					done=true;
				}
				}
				if (!done) {
					newFirstLine.add(actualKey);
			}
		}
		return newFirstLine;
}
		
	public void printCsv (HashMap<Integer,Destinatario> destinatarioMap) {
		 for (Entry<Integer, Destinatario> destinatario: destinatarioMap.entrySet()) {
			 System.out.println(destinatario.getKey()+": "+destinatario.getValue().toString());
		  }
	}

}
	

