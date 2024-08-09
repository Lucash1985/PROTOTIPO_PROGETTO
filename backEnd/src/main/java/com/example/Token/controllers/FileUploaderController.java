package com.example.Token.controllers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.Token.entities.Destinatario;
import com.example.Token.repository.ComuneRepository;
import com.example.Token.services.DestinatarioService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FileUploaderController {

	private final CsvReaderController csvReaderController;
	private final DestinatarioService destinatarioService;
	private final ComuneRepository comuneRepository;
	List<Map<String, String>> rows = new ArrayList<>();
	@PostMapping("/uploadFile")
	public ResponseEntity<ApiResponse<?>> uploadFile(@RequestParam("file") MultipartFile file,
	        @RequestParam HashMap<String, String> params) {

	    try {
	        String fileName = file.getOriginalFilename();
	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "File non valido", null));
	        }

	        // Correggi e normalizza gli header
	        ArrayList<String> newHeader = csvReaderController.csvHeaderCorrection(params,
	                csvReaderController.getCsvHeader(file));
	        if (!newHeader.isEmpty()) {
	            file = csvReaderController.replaceCsvHeader(file, newHeader);
	        }

	        ArrayList<String> normalizedHeader = csvReaderController
	                .csvHeaderNormalizer(csvReaderController.getCsvHeader(file));
	        if (normalizedHeader.size() > 0) {
	            return ResponseEntity.ok(new ApiResponse<>(false, "Header non riconosciuti", normalizedHeader));
	        }

	        file = csvReaderController.replaceCsvHeader(file, normalizedHeader);
	        HashMap<Integer, Destinatario> destinatarioMap = csvReaderController.csvToList(file);
	        HashMap<Integer, Destinatario> destinatarioMapControlName = destinatarioService
	                .controlName(destinatarioMap);
	        HashMap<Integer, Destinatario> destinatarioMapControlComune = destinatarioService
	                .controlCap(destinatarioMap);

	        HashMap<Integer, Destinatario> destinatarioMapControlTotal = new HashMap<>();
	        destinatarioMapControlName.forEach(destinatarioMapControlTotal::putIfAbsent);
	        destinatarioMapControlComune.forEach(destinatarioMapControlTotal::putIfAbsent);

	        if (!destinatarioMapControlTotal.isEmpty()) {
	            return ResponseEntity.ok(new ApiResponse<>(false, "Errors in CSV", destinatarioMapControlTotal));
	        }

	        // Processa i dati CSV corretti
	        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
	        List<Map<String, String>> rows = new ArrayList<>();

	        try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
	             CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build()) {

	            // Estrai gli header
	            String[] headers = csvReader.readNext();

	            // Estrai le righe
	            String[] nextLine;
	            while ((nextLine = csvReader.readNext()) != null) {
	                Map<String, String> rowMap = new HashMap<>();
	                for (int j = 0; j < headers.length; j++) {
	                    rowMap.put(headers[j], j < nextLine.length ? nextLine[j] : "");
	                }
	                rows.add(rowMap);
	            }
	        }

	        // Stampa per debugging (opzionale)
	        for (Map<String, String> row : rows) {
	            System.out.println("Row:");
	            for (Map.Entry<String, String> entry : row.entrySet()) {
	                System.out.println(entry.getKey() + ": " + entry.getValue());
	            }
	            System.out.println(); // Righe vuote tra le righe
	        }

	        return ResponseEntity.ok(new ApiResponse<>(true, "File caricato con successo: " + fileName, rows));

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse<>(false, "Errore durante l'upload del file", null));
	    }
	}

 
	


    @GetMapping("/getRows")
    public ResponseEntity<List<Map<String, String>>> getRows() {
    	 for (Map<String, String> row : rows) {
             System.out.println("Row:");
             for (Map.Entry<String, String> entry : row.entrySet()) {
                 System.out.println(entry.getKey() + ": " + entry.getValue());
             }
             System.out.println(); // Righe vuote tra le righe
         }
        return ResponseEntity.ok(rows);
    }
	
	/*
	 * @PostMapping("/uploadFile") public
	 * ResponseEntity<ApiResponse<List<Map<String, String>>>> uploadFile(
	 * 
	 * @RequestParam("file") MultipartFile file,
	 * 
	 * @RequestParam HashMap<String, String> params) { try { if (file.isEmpty()) {
	 * return ResponseEntity.badRequest().body(new ApiResponse<>(false,
	 * "File non valido", null)); }
	 * 
	 * // Leggi e processa il file CSV CSVReader csvReader = new CSVReader(new
	 * InputStreamReader(file.getInputStream())); List<String[]> csvData =
	 * csvReader.readAll(); csvReader.close(); // for // Se non ci sono dati,
	 * restituisci un messaggio di errore if (csvData.isEmpty()) { return
	 * ResponseEntity.ok(new ApiResponse<>(false, "File CSV vuoto", null)); }
	 * 
	 * // Estrai le intestazioni String[] headers = csvData.get(0); List<Map<String,
	 * String>> rows = new ArrayList<>();
	 * 
	 * // Elenco delle righe del CSV, escluse le intestazioni l'intestazione è
	 * nell'header for (int i = 1; i < csvData.size(); i++) { String[] row =
	 * csvData.get(i); Map<String, String> rowMap = new HashMap<>(); for (int j = 0;
	 * j < headers.length; j++) { rowMap.put(headers[j], row[j]); }
	 * rows.add(rowMap); System.out.println("Row " + i + ": " + rowMap); }
	 * 
	 * // Restituisci i dati CSV nella risposta return ResponseEntity.ok(new
	 * ApiResponse<>(true, "File caricato con successo: " +
	 * file.getOriginalFilename(), rows));
	 * 
	 * } catch (Exception e) { return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new
	 * ApiResponse<>(false, "Errore durante l'upload del file", null)); } } /*
	 * 
	 * @PostMapping("/uploadFile") public ResponseEntity<ApiResponse<?>>
	 * uploadFile(@RequestParam("file") MultipartFile file,
	 * 
	 * @RequestParam HashMap<String, String> params) { try { if (file.isEmpty()) {
	 * return ResponseEntity.badRequest().body(new ApiResponse<>(false,
	 * "File non valido", null)); } //ccontrollo
	 * System.out.println("file arrivato");
	 * 
	 * 
	 * 
	 * String fileName = file.getOriginalFilename(); //normalizza prima riga /* if
	 * (!params.isEmpty()) { ArrayList<String> newHeader =
	 * csvReaderController.csvHeaderCorrection(params,
	 * csvReaderController.getCsvHeader(file)); file =
	 * csvReaderController.replaceCsvHeader(file, newHeader);
	 * 
	 * }
	 * 
	 * // creo oggetto classe CSVReader per leggere file CSVReader csvReader = new
	 * CSVReader(new InputStreamReader(file.getInputStream())); //metto dati
	 * contenuti nel csv in n List<String[]> csvData = csvReader.readAll();
	 * csvReader.close(); for (String[] row : csvData) {
	 * System.out.println(String.join(", ", row)); } // Convert CSV data to a list
	 * of maps (or any other structure you need) List<Map<String, String>>
	 * parsedData = parseCsvData(csvData);
	 * 
	 * ArrayList<String> newHeader =
	 * csvReaderController.csvHeaderNormalizer(csvReaderController.getCsvHeader(file
	 * )); if (newHeader.contains("Header non riconosciuti: ")) { return
	 * ResponseEntity.ok(new ApiResponse<>(false, "Header non riconosciuti",
	 * newHeader)); } for(String h:newHeader) { System.out.println(h); } file =
	 * csvReaderController.replaceCsvHeader(file, newHeader); HashMap<Integer,
	 * Destinatario> destinatarioMap = csvReaderController.csvToList(file);
	 * HashMap<Integer, Destinatario> destinatarioMapControlName =
	 * destinatarioService.controlName(destinatarioMap); HashMap<Integer,
	 * Destinatario> destinatarioMapControlComune =
	 * destinatarioService.controlCap(destinatarioMap);
	 * 
	 * HashMap<Integer, Destinatario> destinatarioMapControlTotal = new HashMap<>();
	 * destinatarioMapControlName.forEach(destinatarioMapControlTotal::putIfAbsent);
	 * destinatarioMapControlComune.forEach(destinatarioMapControlTotal::putIfAbsent
	 * ); for(String h :newHeader ) { System.out.println(h);
	 * 
	 * } if (!destinatarioMapControlTotal.isEmpty()) { return ResponseEntity.ok(new
	 * ApiResponse<>(false, "Errors in CSV", destinatarioMapControlTotal)); }
	 * 
	 * destinatarioMap.forEach((key, value) ->
	 * System.out.println(value.toString())); return ResponseEntity.ok(new
	 * ApiResponse<>(true, "File caricato con successo: " + fileName, null));
	 * 
	 * } catch (Exception e) { return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new
	 * ApiResponse<>(false, "Errore durante l'upload del file", null)); } }
	 */
	/*
	@GetMapping("/downloadCorrectedCsv")
    public void downloadCorrectedCsv(@RequestParam("file") MultipartFile file,
                                     @RequestParam HashMap<String, String> params,
                                     HttpServletResponse response) throws IOException, CsvException {

        // Step 1: Read and correct the CSV headers
        ArrayList<String> newHeader = csvReaderController.csvHeaderCorrection(params, csvReaderController.getCsvHeader(file));
        MultipartFile correctedFile = csvReaderController.replaceCsvHeader(file, newHeader);

        // Step 2: Generate CSV content with corrected headers
        try (InputStreamReader reader = new InputStreamReader(correctedFile.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            List<String[]> csvData = csvReader.readAll();
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer,
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            // Write headers
            csvWriter.writeNext(newHeader.toArray(new String[0]));

            // Write data rows
            for (String[] row : csvData) {
                csvWriter.writeNext(row);
            }

            csvWriter.close();

            // Step 3: Send CSV file as response
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=corrected_file.csv");

            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(writer.toString().getBytes());
                outputStream.flush();
            }
        }
    }
	*/
	private List<Map<String, String>> parseCsvData(List<String[]> csvData) {
		List<Map<String, String>> result = new ArrayList<>();
		if (csvData.isEmpty())
			return result;

		String[] headers = csvData.get(0); // Assuming the first row is the header
		for (int i = 1; i < csvData.size(); i++) {
			String[] row = csvData.get(i);
			Map<String, String> rowMap = new HashMap<>();
			for (int j = 0; j < headers.length; j++) {
				rowMap.put(headers[j], row[j]);
			}
			result.add(rowMap);
		}
		return result;
	}

	private String getFileExtension(String fileName) {
		try {
			String parti[] = fileName.split(".");
			for (String parte : parti)
				System.out.println(parte);
			return parti[1];
		} catch (Exception e) {
			// TODO: handle exception
		}
		{

		}
		return "";
	}
}