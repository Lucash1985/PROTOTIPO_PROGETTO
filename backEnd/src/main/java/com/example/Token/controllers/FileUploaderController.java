package com.example.Token.controllers;

import java.io.InputStreamReader;
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
import com.opencsv.CSVReader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FileUploaderController {

	private final CsvReaderController csvReaderController;
private final DestinatarioService destinatarioService;
	private final ComuneRepository comuneRepository;
	
	
	
	
	@PostMapping("/uploadFile")
	public ResponseEntity<ApiResponse<?>> uploadFile(@RequestParam("file") MultipartFile file,
	                                                 @RequestParam HashMap<String, String> params) {
	    try {
	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "File non valido", null));
	        }

	        String fileName = file.getOriginalFilename();
	        if (!params.isEmpty()) {
	            ArrayList<String> newHeader = csvReaderController.csvHeaderCorrection(params,
	                    csvReaderController.getCsvHeader(file));
	            file = csvReaderController.replaceCsvHeader(file, newHeader);
	        }

	        ArrayList<String> newHeader = csvReaderController.csvHeaderNormalizer(csvReaderController.getCsvHeader(file));
	        if (newHeader.size()>0) {
	            return ResponseEntity.ok(new ApiResponse<>(false, "Header non riconosciuti", newHeader));
	        }

	        file = csvReaderController.replaceCsvHeader(file, newHeader);
	        HashMap<Integer, Destinatario> destinatarioMap = csvReaderController.csvToList(file);
	        HashMap<Integer, Destinatario> destinatarioMapControlName = destinatarioService.controlName(destinatarioMap);
	        HashMap<Integer, Destinatario> destinatarioMapControlComune = destinatarioService.controlCap(destinatarioMap);

	        HashMap<Integer, Destinatario> destinatarioMapControlTotal = new HashMap<>();
	        destinatarioMapControlName.forEach(destinatarioMapControlTotal::putIfAbsent);
	        destinatarioMapControlComune.forEach(destinatarioMapControlTotal::putIfAbsent);

	        if (!destinatarioMapControlTotal.isEmpty()) {
	            return ResponseEntity.ok(new ApiResponse<>(false, "Errors in CSV", destinatarioMapControlTotal));
	        }

	        // Process the CSV data
	        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
	        List<String[]> csvData = csvReader.readAll();
	        csvReader.close();

	        // Headers
	        String[] headers = csvData.get(0);
	        List<Map<String, String>> rows = new ArrayList<>();

	        // Extract rows
	        for (int i = 1; i < csvData.size(); i++) {
	            String[] row = csvData.get(i);
	            Map<String, String> rowMap = new HashMap<>();
	            for (int j = 0; j < headers.length; j++) {
	                rowMap.put(headers[j], row[j]);
	            }
	            rows.add(rowMap);
	            
	            System.out.println("Row " + i + ": " + rowMap);
	        }

	        return ResponseEntity.ok(new ApiResponse<>(true, "File caricato con successo: " + fileName, rows));

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse<>(false, "Errore durante l'upload del file", null));
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	@PostMapping("/uploadFile")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam HashMap<String, String> params) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "File non valido", null));
            }

            // Leggi e processa il file CSV
            CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
            List<String[]> csvData = csvReader.readAll();
            csvReader.close();
          //  for
            // Se non ci sono dati, restituisci un messaggio di errore
            if (csvData.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse<>(false, "File CSV vuoto", null));
            }

            // Estrai le intestazioni
            String[] headers = csvData.get(0);
            List<Map<String, String>> rows = new ArrayList<>();

            // Elenco delle righe del CSV, escluse le intestazioni l'intestazione è nell'header
            for (int i = 1; i < csvData.size(); i++) {
                String[] row = csvData.get(i);
                Map<String, String> rowMap = new HashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    rowMap.put(headers[j], row[j]);
                                    }
                rows.add(rowMap);
                System.out.println("Row " + i + ": " + rowMap);
            }
               
            // Restituisci i dati CSV nella risposta
            return ResponseEntity.ok(new ApiResponse<>(true, "File caricato con successo: " + file.getOriginalFilename(), rows));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Errore durante l'upload del file", null));
        }
    }
	/*
	@PostMapping("/uploadFile")
	public ResponseEntity<ApiResponse<?>> uploadFile(@RequestParam("file") MultipartFile file,
	                                                 @RequestParam HashMap<String, String> params) {
	    try {
	        if (file.isEmpty()) {
	            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "File non valido", null));
	        }  
	      //ccontrollo  
            System.out.println("file arrivato");
  
 
         
	        String fileName = file.getOriginalFilename();
	        //normalizza prima riga
	       /* if (!params.isEmpty()) {
	            ArrayList<String> newHeader = csvReaderController.csvHeaderCorrection(params,
	                    csvReaderController.getCsvHeader(file));
	            file = csvReaderController.replaceCsvHeader(file, newHeader);
	            
	        }
	      
	        // creo oggetto classe CSVReader per leggere file 
	        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
	        //metto dati contenuti nel csv in n
	        List<String[]> csvData = csvReader.readAll();
	        csvReader.close();
	        for (String[] row : csvData) {
	            System.out.println(String.join(", ", row));
	        }
	        // Convert CSV data to a list of maps (or any other structure you need)
	        List<Map<String, String>> parsedData = parseCsvData(csvData);
	       
	        ArrayList<String> newHeader = csvReaderController.csvHeaderNormalizer(csvReaderController.getCsvHeader(file));
	        if (newHeader.contains("Header non riconosciuti: ")) {
	            return ResponseEntity.ok(new ApiResponse<>(false, "Header non riconosciuti", newHeader));
	        }
	        for(String h:newHeader) {
            	System.out.println(h);
            }
	        file = csvReaderController.replaceCsvHeader(file, newHeader);
	        HashMap<Integer, Destinatario> destinatarioMap = csvReaderController.csvToList(file);
	        HashMap<Integer, Destinatario> destinatarioMapControlName = destinatarioService.controlName(destinatarioMap);
	        HashMap<Integer, Destinatario> destinatarioMapControlComune = destinatarioService.controlCap(destinatarioMap);

	        HashMap<Integer, Destinatario> destinatarioMapControlTotal = new HashMap<>();
	        destinatarioMapControlName.forEach(destinatarioMapControlTotal::putIfAbsent);
	        destinatarioMapControlComune.forEach(destinatarioMapControlTotal::putIfAbsent);
             for(String h :newHeader ) {
            	 System.out.println(h);
            	 
             }
	        if (!destinatarioMapControlTotal.isEmpty()) {
	            return ResponseEntity.ok(new ApiResponse<>(false, "Errors in CSV", destinatarioMapControlTotal));
	        }

	        destinatarioMap.forEach((key, value) -> System.out.println(value.toString()));
	        return ResponseEntity.ok(new ApiResponse<>(true, "File caricato con successo: " + fileName, null));

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ApiResponse<>(false, "Errore durante l'upload del file", null));
	    }
	}
	*/
	private List<Map<String, String>> parseCsvData(List<String[]> csvData) {
	    List<Map<String, String>> result = new ArrayList<>();
	    if (csvData.isEmpty()) return result;

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