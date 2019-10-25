package com.example.usphuong.flicks.mapper;



import com.example.usphuong.flicks.api.ApiService;
import com.example.usphuong.flicks.entity.NowPlaying;
import com.example.usphuong.flicks.entity.Videos;
import com.example.usphuong.flicks.model.Movie;
import com.example.usphuong.flicks.model.Trailer;

import java.util.ArrayList;
import java.util.List;


/**
 * This class knows how to transform a dataserver entity to a model entity
 */
public class DataMapper {
    /**
     * Transform a movie list details movieList entity to a business details model
     *
     * @param dto
     * @return
     */
    public Movie transform(NowPlaying.MovieListDTO dto) {
        return new Movie(dto.getId(),
                dto.getVoteAverage(),
                dto.getTitle(),
                createPoster(dto.getPosterPath()),
                createBackdrop(dto.getBackdropPath()),
                dto.getOverview(),
                dto.getReleaseDate()
        );
    }

    /**
     * Transform a movie list movieList entity to a business list model
     *
     * @param dto
     * @return
     */
    public List<Movie> transform(NowPlaying dto) {
        List<Movie> movies = new ArrayList<>();
        List<NowPlaying.MovieListDTO> dtoList = dto.getResults();
        for (NowPlaying.MovieListDTO movie : dtoList) {
            movies.add(transform(movie));
        }
        return movies;
    }

    /**
     * Transform a listVideo details  entity to a business details model
     *
     * @param dto
     * @return
     */
    public Trailer transform(Videos.VideoListDTO dto) {
        return new Trailer(dto.getId(),
                dto.getKey(),
                dto.getName(),
                dto.getSize(),
                dto.getType()
        );
    }


    /**
     * Transform a movie list movieList entity to a business list model
     *
     * @param dto
     * @return
     */
    public List<Trailer> transform(Videos dto) {
        List<Trailer> trailer = new ArrayList<>();
        List<Videos.VideoListDTO> dtoList = dto.getResults();
        for (Videos.VideoListDTO video : dtoList) {
            if(video.getType().equals("Trailer")) {
                trailer.add(transform(video));
            }
        }
        return trailer;
    }






    /**
     * Transform a relative path to a complete URI image
     *
     * @param path
     * @return
     */
    private String createPoster(String path) {
        if (path == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiService.BASE_IMAGES_URL);
        stringBuilder.append(ApiService.POSTER_SIZE);
        stringBuilder.append(path);
        return stringBuilder.toString();
    }
    private String createBackdrop(String path) {
        if (path == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiService.BASE_IMAGES_URL);
        stringBuilder.append(ApiService.BACKDROP_SIZE);
        stringBuilder.append(path);
        return stringBuilder.toString();
    }
    private String createLinkYoutube(String path) {
        if (path == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiService.BASE_YOUTUBE_URL);
        stringBuilder.append(path);
        return stringBuilder.toString();
    }



}
