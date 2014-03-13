package ru.apress.productslist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shushper on 12.03.14.
 */
public class Utils {

    public static boolean saveInputStreamToFile(InputStream in, File file) {
        boolean success = true;
        OutputStream out = null;

        try {
            try {
                out = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[1024];
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }

                out.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                success = false;
            } catch (IOException e) {
                e.printStackTrace();
                success = false;
            } finally {
                if (out != null) out.close();
                if (in != null) in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public static String readFile(File file) {
        String result = null;
        BufferedReader reader = null;

        try {
            try {
                reader = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                result = sb.toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public static Product[] parseJson(String jsonStr){
        Product[] productObjs = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject content = jsonObject.getJSONObject("content");
            JSONArray productsArr = content.getJSONArray("products");
            productObjs = new Product[productsArr.length()];

            for(int i = 0; i < productsArr.length(); i++) {
                Product productObj = new Product();

                JSONObject product = productsArr.getJSONObject(i);
                productObj.setId(product.getInt("id"));
                productObj.setName(product.getString("name"));
                productObj.setImagesCnt(product.getInt("images_cnt"));

                JSONArray imagesArr = product.getJSONArray("images");
                Image[] imageObjs = new Image[imagesArr.length()];

                for (int k = 0; k < imagesArr.length(); k++) {
                    Image imageObj = new Image();

                    JSONObject image = imagesArr.getJSONObject(k);
                    imageObj.setId(image.getInt("id"));
                    imageObj.setPos(image.getInt("position"));
                    imageObj.setPathThumb(image.getString("path_thumb"));
                    imageObj.setPathBig(image.getString("path_big"));

                    imageObjs[k] = imageObj;
                }

                productObj.setImages(imageObjs);
                productObjs[i] = productObj;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            productObjs = null;
        }
        return productObjs;
    }
}
