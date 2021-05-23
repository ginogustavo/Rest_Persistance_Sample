import model.Cat;
import model.CatFav;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.CatService;

import java.io.IOException;

public class SimpleRequest {

    final String API_URL = "https://api.thecatapi.com";
    final String VERSION = "/v1";
    final String BASE_URL = API_URL + VERSION;
    OkHttpClient client;

    @Before
    public void init() {
        client = new OkHttpClient();
        //client = new OkHttpClient().newBuilder().build();
    }


    @Test
    public void whenGetRequest_thenCorrect() throws IOException {

        Request request = new Request.Builder()
                .url(BASE_URL + "/images/search")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        Assert.assertEquals(response.code(), 200);
    }

    @Test
    public void whenGetRequestWithResponse_thenCorrect() throws IOException {

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String content_type = response.body().contentType().toString();
        //response.body().string(); --> get json string

        Assert.assertTrue(content_type.startsWith("application/json;"));

    }

    @Test
    public void whenGetCatService_thenCorrect() throws IOException {

        String photo_url = new CatService().getRandomCatURLImage();
        Assert.assertNotNull(photo_url);
        Assert.assertTrue(photo_url.startsWith("https://cdn2.thecatapi.com/images/"));

    }

    @Test
    public void whenPostSetFavorite_thenSuccess() throws IOException {
        Cat cat = new Cat();
        cat.setId("ck3");
        String json = new CatService().setFavorite(cat);

        Assert.assertTrue(json.contains("SUCCESS"));
        //TODO: Strategy of testing post
        // Later we must delete, and it could be delete right away, if we need to test delete
        // or at the end of testing delete all entities flagged as TESTs

    }

    @Test
    public void whenPostSetFavorite_thenListedInGetFavorites() throws IOException {
        String ID_TEST = "P-EHkzVJb";
        CatService service = new CatService();
        Cat cat = new Cat();
        cat.setId(ID_TEST);
        String json = service.setFavorite(cat);

        Assert.assertTrue(json.contains("SUCCESS"));

        CatFav[] favorites = service.getFavorites();
        boolean newFavoriteFound = false;
        for (CatFav catfav:
             favorites) {
            if(catfav.getImage_id().equals( ID_TEST)){
                newFavoriteFound = true;
                break;
            }
        }
        Assert.assertTrue(newFavoriteFound);


    }

    @Test
    public void whenGetFavorites_thenCorrect() throws IOException {

        CatFav[] favorites = new CatService().getFavorites();
        String urlDemo = favorites[0].getImage().getUrl();
        Assert.assertTrue(urlDemo.startsWith("https://cdn2.thecatapi.com/images/"));
    }

}
