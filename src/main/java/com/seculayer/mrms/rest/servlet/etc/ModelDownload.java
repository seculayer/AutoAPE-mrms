package com.seculayer.mrms.rest.servlet.etc;

import com.seculayer.mrms.managers.MRMServerManager;
import com.seculayer.mrms.rest.ServletHandlerAbstract;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet("/download")
public class ModelDownload extends ServletHandlerAbstract {
    public static final String ContextPath = ServletHandlerAbstract.ContextPath + "/model_download";
    private final int ARBITARY_SIZE = 1048;

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
        throws ServletException, IOException {

        httpServletResponse.setContentType("text/plain; charset=utf-8");
        logger.debug("###################################################################");
        String id = httpServletRequest.getParameter("learn_hist_no");
        String zipFileName = id + ".zip";
        httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + zipFileName);
        String folderPath = MRMServerManager.getInstance().getConfiguration().get("ape.model.dir", "/eyeCloudAI/data/storage/ape");
        String modelPath = folderPath + "/" + String.format("%s", id);

        try {
            File zipFile = new File(String.format("%s/%s", folderPath, zipFileName));
            if (!zipFile.exists()) {
                boolean rst = compress(modelPath, folderPath, zipFileName);
            }

            InputStream inStream = new FileInputStream(String.format("%s/%s", folderPath, zipFileName));
            OutputStream outStream = httpServletResponse.getOutputStream();

            byte[] buffer = new byte[ARBITARY_SIZE];

            int numBytesRead;
            while ((numBytesRead = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, numBytesRead);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        logger.debug("###################################################################");

    }

    private boolean compress(String path, String outputPath, String outputFileName) throws Throwable {
        // 파일 압축 성공 여부
        boolean isChk = false;

        File file = new File(path);

        // 파일의 .zip이 없는 경우, .zip 을 붙여준다.
        int pos = outputFileName.lastIndexOf(".") == -1 ? outputFileName.length() : outputFileName.lastIndexOf(".");

        // outputFileName .zip이 없는 경우
        if (!outputFileName.substring(pos).equalsIgnoreCase(".zip")) {
            outputFileName += ".zip";
        }

        // 압축 경로 체크
        if (!file.exists()) {
            throw new Exception("Not File!");
        }

        // 출력 스트림
        // 압축 스트림
        try (FileOutputStream fos = new FileOutputStream(new File(outputPath + "/" + outputFileName));
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            // 디렉토리 검색를 통한 하위 파일과 폴더 검색
            searchDirectory(file, file.getPath(), zos);

            // 압축 성공.
            isChk = true;
        }

        return isChk;
    }

    private void searchDirectory(File file, String root, ZipOutputStream zos) throws Exception {
        // 지정된 파일이 디렉토리인지 파일인지 검색
        if (file.isDirectory()) {
            // 디렉토리일 경우 재탐색(재귀)
            File[] files = file.listFiles();
            assert files != null;
            for (File f : files) {
                logger.debug("file = > " + f);
                searchDirectory(f, root, zos);
            }
        } else {
            // 파일일 경우 압축을 한다.
            try {
                compressZip(file, root, zos);
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void compressZip(File file, String root, ZipOutputStream zos) throws Throwable {
        FileInputStream fis = null;
        try {
            String zipName = file.getPath().replace(root + "\\", "");
            zipName = file.getPath().replace(root + "/", "");
            // 파일을 읽어드림
            fis = new FileInputStream(file);
            // Zip엔트리 생성(한글 깨짐 버그)
            ZipEntry zipentry = new ZipEntry(zipName);
            // 스트림에 밀어넣기(자동 오픈)
            zos.putNextEntry(zipentry);
            int length = (int) file.length();
            byte[] buffer = new byte[length];
            // 스트림 읽어드리기
            fis.read(buffer, 0, length);
            // 스트림 작성
            zos.write(buffer, 0, length);
            // 스트림 닫기
            zos.closeEntry();

        } finally {
            if (fis != null)
                fis.close();
        }
    }
}
