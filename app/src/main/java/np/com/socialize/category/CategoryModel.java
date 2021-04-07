package np.com.socialize.category;

import java.util.HashMap;
import java.util.Map;

public class CategoryModel {


    public CategoryModel() {
    }

    String id;
    String name;
    String image;
    String description;
    String serverId;
    String chat;


    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryModel(String id, String name, String image, String description,String chat) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.chat = chat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        if (id != null) {
            map.put("id", id);
        }
        if (name != null) {
            map.put("name", name);
        }
        if (image != null) {
            map.put("image", image);
        }
        if (description != null) {
            map.put("description", description);
        }

        if (chat != null){
              map.put("chat",chat);

        }
        return map;
    }


}
