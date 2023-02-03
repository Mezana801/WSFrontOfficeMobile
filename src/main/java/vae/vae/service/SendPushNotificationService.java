package vae.vae.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import vae.vae.model.Enchere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SendPushNotificationService {

    public static void sendNotifcation(Enchere enchere) throws Exception{
        String url = "https://onesignal.com/api/v1/notifications";
        String message = "Une enchère de "+enchere.getUtilisateurs().getPrenom()+" vient de terminer. Veuillez nous rejoindre ^^";

        HashMap<String, Object> dataNotif = new HashMap<>();
        HashMap<String, String> content = new HashMap<>();
        content.put("en", message);

        HashMap<String, String> headings = new HashMap<>();
        content.put("en", message);

        dataNotif.put("app_id", "51533bda-7d07-4a70-91e6-dcf1b90fcb9a");
        dataNotif.put("included_segments", Arrays.asList("Subscribed Users"));
        dataNotif.put("contents", content);
        dataNotif.put("headings", headings);
        dataNotif.put("name", "INTERNAL_CAMPAIGN_NAME");
//        dataNotif.put("include_external_user_ids", enchere.getUtilisateursid());

        String token = "OGMwMGVlZGEtMmU2MS00M2NmLTg1NjgtZDE4ZjIyZDA5ZWEx";
        List<MediaType> listes = new ArrayList<>();
        listes.add(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(listes);
        headers.set("Authorization", "Basic "+token);

        HttpEntity<HashMap<String, Object> > request = new HttpEntity<>(dataNotif, headers);
        ResponseEntity response = restTemplate.postForEntity(url, request, Object.class);

    }
}
