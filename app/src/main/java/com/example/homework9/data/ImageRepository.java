package com.example.homework9.data;

import com.example.homework9.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

public final class ImageRepository {
    private ImageRepository() {
    }

    public static List<ImageItem> getImages() {
        List<ImageItem> images = new ArrayList<>();

        images.add(new ImageItem(
                "Mountain Lake",
                "Публичное изображение из Lorem Picsum, загружается через Glide и кэшируется локально.",
                "https://picsum.photos/id/1015/900/600"
        ));
        images.add(new ImageItem(
                "Forest Road",
                "Вертикальный список построен на RecyclerView с переиспользованием ViewHolder.",
                "https://picsum.photos/id/1040/900/600"
        ));
        images.add(new ImageItem(
                "City Geometry",
                "При прокрутке Glide повторно использует кэш и не загружает одинаковые изображения заново.",
                "https://picsum.photos/id/1031/900/600"
        ));
        images.add(new ImageItem(
                "Ocean Breeze",
                "Подходит для демонстрации загрузки, отображения и memory/disk cache в одном задании.",
                "https://picsum.photos/id/1056/900/600"
        ));
        images.add(new ImageItem(
                "Autumn Valley",
                "Каждый элемент списка содержит картинку, заголовок и краткое описание.",
                "https://picsum.photos/id/1020/900/600"
        ));
        images.add(new ImageItem(
                "Desert Light",
                "Источник изображений не требует API-ключа, поэтому проект сразу можно запускать.",
                "https://picsum.photos/id/1002/900/600"
        ));
        images.add(new ImageItem(
                "Winter Cabin",
                "Glide использован как основная библиотека загрузки изображений в Android Studio.",
                "https://picsum.photos/id/1003/900/600"
        ));
        images.add(new ImageItem(
                "Bridge View",
                "Элемент списка оформлен через MaterialCardView для аккуратного UI.",
                "https://picsum.photos/id/1060/900/600"
        ));
        images.add(new ImageItem(
                "Calm River",
                "RecyclerView отображает данные эффективно даже при большом количестве картинок.",
                "https://picsum.photos/id/1011/900/600"
        ));
        images.add(new ImageItem(
                "Sunset Coast",
                "Это приложение соответствует заданию Homework9: Glide + RecyclerView + список изображений.",
                "https://picsum.photos/id/1016/900/600"
        ));

        return images;
    }
}
