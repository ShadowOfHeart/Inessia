package gg.vape.profile;

import gg.vape.Medusa;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static gg.vape.helpers.Helper.mc;

public class Profile {

    public static File file = new File("C:\\Vape Client", "profile.cfg");
    public static String[] data;
    public static UserInfo info;
    public static String ss = "";
    public Profile(){

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
            }
        }


    }

    public static void save(String username, int uid, String title, String version, String till, int balance, boolean subscription, String password) {
        info = new UserInfo(username, uid, title, version, till, balance, subscription, password);
    }
    public static void reset(){
        info = new UserInfo(null, 0, null, null, null, 0, false, null);
    }

    public static void load() {

//        if(info.getUid() == 0 && info.getUsername() == null) {
//            info = new UserInfo("KseNon", 1, "Admin", "(Beta) 1.1", "2049.5.26", 1, true, "qwe123");
//        }




        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        try {

            if(ss == null) {
                ss += (info.getUsername() + ":" + info.getUid() + ":" + info.getTitle() + ":" + info.getVersion() + ":" + info.getTill() + ":" + info.getBalance() + ":" + info.getSubscription() + ":" + info.getPassword()) + "\n";
                Files.write(file.toPath(), ss.getBytes());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Failed to load profile");
        }

        String[] data;
        try {
            data = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).split(":");
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
//        if (!info.getSubscription() || info.getUsername() == null || info.getUid() == 0) {
//            mc.shutdown();
//        }
        if ((Integer.parseInt(data[1]) == 0) && (data[0] == null) && (Boolean.parseBoolean(data[6]) == false)) {
            mc.shutdown();
        }

        Medusa.username = data[0];
        Medusa.uid = Integer.parseInt(data[1]);
        Medusa.title = data[2];
        Medusa.version = data[3];
        Medusa.till = data[4];
        Medusa.balance = Integer.parseInt(data[5]);
        Medusa.subscription = Boolean.parseBoolean(data[6]);
        Medusa.password = data[7];

    }

}
