package com.example.euromessagejavav2.Models;

import java.util.List;

public class ProductModel {
    private String id;
    private String sku;
    private String name;
    private String attribute_set_id;
    private String price;
    private String status;
    private String visibility;
    private String type_id;
    private String created_at;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    public ProductModel(String name, String price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute_set_id() {
        return attribute_set_id;
    }

    public void setAttribute_set_id(String attribute_set_id) {
        this.attribute_set_id = attribute_set_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ExtensitonAttribute getExtension_attributes() {
        return extension_attributes;
    }

    public void setExtension_attributes(ExtensitonAttribute extension_attributes) {
        this.extension_attributes = extension_attributes;
    }

    public List<ProductLink> getProduct_links() {
        return product_links;
    }

    public void setProduct_links(List<ProductLink> product_links) {
        this.product_links = product_links;
    }

    public List<MediaGalleryEntry> getMedia_gallery_entries() {
        return media_gallery_entries;
    }

    public void setMedia_gallery_entries(List<MediaGalleryEntry> media_gallery_entries) {
        this.media_gallery_entries = media_gallery_entries;
    }

    private String updated_at;
    private ExtensitonAttribute extension_attributes;
    private List<ProductLink> product_links;
    private List<MediaGalleryEntry> media_gallery_entries;

    public static class ExtensitonAttribute {
        private int[] website_ids;
    }

    public static class ProductLink {
        private String sku;
        private String link_type;
        private String linked_product_sku;
        private String linked_product_type;
        private int position;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getLinked_product_sku() {
            return linked_product_sku;
        }

        public void setLinked_product_sku(String linked_product_sku) {
            this.linked_product_sku = linked_product_sku;
        }

        public String getLinked_product_type() {
            return linked_product_type;
        }

        public void setLinked_product_type(String linked_product_type) {
            this.linked_product_type = linked_product_type;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public static class MediaGalleryEntry {
        private int id;
        private String media_type;
        private String label;
        private int position;
        private boolean disabled;
        private String[] types;
        private String file;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public String[] getTypes() {
            return types;
        }

        public void setTypes(String[] types) {
            this.types = types;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }
    }
}
