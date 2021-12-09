package com.seculayer.mrms.info;

import com.seculayer.mrms.managers.MRMServerManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public abstract class InfoAbstract {
    protected Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public InfoAbstract() {}
    protected File infoFile = null;
    protected String outputDir = MRMServerManager.getInstance().getConfiguration().get("ape.job.dir");

    protected String key = "";
    public InfoAbstract(String key){
        this.key = key;
    }
    public String getKey() { return this.key; }

    public abstract void init(Map<String, Object> map);
    public abstract Map<String, Object> makeInfoMap();
    public String toString(){
        return this.makeInfoMap().toString();
    }

    protected File infoFile() {
        if (this.infoFile == null){
            infoFile = new File(outputDir, this.key + ".info");
        }
        return infoFile;
    }

    public void writeInfo() throws IOException {
        BufferedOutputStream outputStream = null;
        try {
            File _infoFile = infoFile();
            String path = _infoFile.getPath();
            File dir_path = _infoFile.getParentFile();
            if (!dir_path.exists()) {
                dir_path.mkdirs();
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(path));

            JSONObject jsonData = new JSONObject();
            for (Map.Entry<String, Object> entry : this.makeInfoMap().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                jsonData.put(key, value);
            }
            outputStream.write(jsonData.toString(2).getBytes());
            logger.info("{} - write.", path);

        } catch (Exception e){
            e.printStackTrace();

        } finally {
            if (outputStream != null){
                outputStream.close();
            }
        }
    }

    public abstract Map<String, Object> loadInfo(String key);
}
