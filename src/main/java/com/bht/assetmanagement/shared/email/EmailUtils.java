package com.bht.assetmanagement.shared.email;

import com.bht.assetmanagement.persistence.entity.Asset;
import com.bht.assetmanagement.persistence.entity.AssetInquiry;
import com.bht.assetmanagement.persistence.entity.Storage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class EmailUtils {

    public String getSubjectActivationText() {
        return "Aktivierungslink";
    }

    public String getBodyActivationText(String token) throws MalformedURLException {
        URL activationUrl = new URL("http://localhost:8080/api/v1/auth/activate/account/" + token);
        return "Please click on the below url to activate your account: " + activationUrl;
    }

    public String getSubjectIsEnabledText() {
        return "Der Status Ihrer Asset Anfrage hat sich geändert.";
    }

    public String getBodyEnabledText() {
        return "Der Status Ihrer Asset Anfrage hat sich geändert. Ihre Anfrage wurde angenommen.";
    }

    public String getBodyDisabledText() {
        return "Der Status Ihrer Asset Anfrage hat sich geändert. Leider musste Ihre Anfrage abgelehnt werden";
    }

    public String getSubjectNewAssetInquiryText() {
        return "Neue AssetAnfrage";
    }

    public String getBodyNewAssetInquiryText() {
        return "Sie haben eine neue Asset Anfrage erhalten.";
    }

    public String getSubjectOrderNotificationText() {
        return "Asset Anfrage Erinnerung";
    }

    public String getBodyOrderNotificationText(AssetInquiry assetInquiry) {
        return "Sie haben eine Asset Anfrage für den Mitarbeiter:\n "
                + assetInquiry.getOwner().getFirstName()
                + " "
                + assetInquiry.getOwner().getLastName()
                + "\n bearbeitet. Bitte vergessen Sie nicht das Asset:\n "
                + assetInquiry.getAssetName()
                + " an die Adresse: \n "
                + assetInquiry.getAddress().getStreetName() + " " + assetInquiry.getAddress().getStreetNumber()
                + "\n"
                + assetInquiry.getAddress().getPostalCode() + " " + assetInquiry.getAddress().getCity()
                + "\n"
                + assetInquiry.getAddress().getCountry()
                + "\n zu schicken.";
    }

    public String getBodyOrderAndStorageRemoveNotificationText(AssetInquiry assetInquiry, Storage storage) {
        return "Sie haben eine Asset Anfrage für den Mitarbeiter:\n "
                + assetInquiry.getOwner().getFirstName()
                + " "
                + assetInquiry.getOwner().getLastName()
                + "\n bearbeitet. Bitte vergessen Sie nicht das Asset:\n "
                + assetInquiry.getAssetName()
                + "\n aus dem Lager: "
                + storage.getName()
                + "\n zu entnehmen und an die Adresse: \n "
                + assetInquiry.getAddress().getStreetName() + " " + assetInquiry.getAddress().getStreetNumber()
                + "\n"
                + assetInquiry.getAddress().getPostalCode() + " " + assetInquiry.getAddress().getCity()
                + "\n"
                + assetInquiry.getAddress().getCountry()
                + "\n zu schicken.";
    }

    public String getSubjectNewUserAccount() {
        return "Neuer User Account";
    }

    public String getBodyNewUserAccount(String email, String passwort) throws MalformedURLException {
        final PasswordEncoder passwordEncoder;

        URL activationUrl = new URL("http://localhost:8080/auth/signin");

        return "Es wurde für Sie ein neuer User Account für MyAssets erstellt. Bitte ändern Sie Ihr Passwort nach der ersten anmeldung.\n Anmeldedaten: "
                + "\n E-Mail: " + email
                + "\n Passwort: " + passwort
                + "\n\n\n klicken Sie den Link an: " + activationUrl + "um sich anzumelden.";
    }

    public String getSubjectRomeveAsset() {
        return "Asset Rückgabe";
    }

    public String getBodyRemoveAsset(Asset asset, Storage storage) {
        return "Vergessen Sie nicht ihr Asset: \n"
                + asset.getName() + "\n in das Storage: \n"
                + storage.getName()
                + "\n zu legen.";
    }
}
