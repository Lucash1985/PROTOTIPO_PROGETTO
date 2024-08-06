package com.example.Token.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.Token.entities.Comune;
import com.example.Token.entities.Destinatario;
import com.example.Token.repository.ComuneRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DestinatarioService {

	private final ComuneRepository comuneRepository;

	public HashMap<Integer, Destinatario> controlName(HashMap<Integer, Destinatario> destinatarioMap) {
		HashMap<Integer, Destinatario> errorMap = new HashMap<Integer, Destinatario>();
		HashMap<Integer, Destinatario> newMap = new HashMap<Integer, Destinatario>();

		for (Entry<Integer, Destinatario> entry : destinatarioMap.entrySet()) {
			String ragioneSociale = entry.getValue().getRagioneSociale();
			String nome = entry.getValue().getNome();
			String cognome = entry.getValue().getCognome();
			if (ragioneSociale.equalsIgnoreCase("")) {
				if (!nome.equalsIgnoreCase("") && !cognome.equalsIgnoreCase("")) {
					Destinatario d = entry.getValue();
					d.setRagioneSociale(cognome + " " + nome);
					d.setNome("");
					d.setCognome("");
					newMap.put(entry.getKey(), d);
				} else {
					Destinatario d = entry.getValue();
					d.setRagioneSociale("<h4>"+entry.getValue().getRagioneSociale()+"</h4>");
					d.setNome("<h4>"+entry.getValue().getNome()+"</h4>");
					d.setCognome("<h4>"+entry.getValue().getCognome()+"</h4>");
					errorMap.put(entry.getKey(), d);
				}
			} else {
				Destinatario d = entry.getValue();
				d.setNome("");
				d.setCognome("");
				newMap.put(entry.getKey(), d);
			}

		}
		if (!errorMap.isEmpty()) {
			errorMap.put(0, new Destinatario());
			return errorMap;
		} else
			return newMap;
	}

	public HashMap<Integer, Destinatario> controlCap(HashMap<Integer, Destinatario> destinatarioMap) {
		HashMap<Integer, Destinatario> errorMap = new HashMap<Integer, Destinatario>();

		for (Entry<Integer, Destinatario> entry : destinatarioMap.entrySet()) {
			String cap = entry.getValue().getCap();
			String citta = entry.getValue().getCittà();
			String provincia = entry.getValue().getProvincia();
			String stato = entry.getValue().getStato();
			String indirizzo = entry.getValue().getIndirizzo();
			if (cap.equalsIgnoreCase("")||indirizzo.equalsIgnoreCase("")) {
				errorMap.put(entry.getKey(), entry.getValue());
			}else {
				boolean isComuneOk = false;
				for (Comune c: comuneRepository.findByCap(cap)) {
					if (c.getCittà().equalsIgnoreCase(citta)&&c.getProvincia().equalsIgnoreCase(provincia)&&c.getStato().equalsIgnoreCase(stato)) {
						isComuneOk = true;
					}
				}if (!isComuneOk) {
					errorMap.put(entry.getKey(), entry.getValue());
				}
			}
		}
		if (!errorMap.isEmpty()) {
			errorMap.put(0, new Destinatario());
			return errorMap;
		} else
			return errorMap;
	}
}
