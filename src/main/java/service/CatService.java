package service;

import com.google.gson.Gson;
import model.Cat;
import model.CatFav;
import okhttp3.*;

import java.io.IOException;

public class CatService {

    OkHttpClient client = new OkHttpClient();

    public String getRandomCatURLImage() throws IOException {

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        //trim square brackets
        json = json.substring(1);
        json = json.substring(0,json.length()-1);

        //Create object of Gson class
        Gson gson = new Gson();
        Cat cat = gson.fromJson( json, Cat.class);

        return cat.getUrl();
    }


    public String setFavorite(Cat cat) throws IOException{

        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\n  \"image_id\": \"MTc2MDI4MA\"\n}");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"image_id\": \""+cat.getId()+"\"\n}");
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites?")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", "8a41dbab-5f95-45fa-8f11-bd14520db953")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public CatFav[] getFavorites() throws IOException{
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("x-api-key", "8a41dbab-5f95-45fa-8f11-bd14520db953")
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();

        Gson gson = new Gson();
        CatFav[] catFavorites = gson.fromJson( json, CatFav[].class);
        return catFavorites;
    }
}
