package sai.com.popularmovies.Model;

import java.util.List;

/**
 * Created by krrish on 1/11/2016.
 */

public class Movies {
    List<results> results;

    public List<results> getResults() {
        return results;
    }

    public void setMovies(List<results> results) {
        this.results = results;
    }

    public  class results {
        String poster_path;
        String overview;
        String release_date;
        int id;
        String original_title;
        String original_language;
        String title;
        String backdrop_path;
        double popularity;
        int vote_count;
        double vote_average;

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public int getVote_count() {
            return vote_count;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public double getVote_average() {
            return vote_average;
        }

        public void setVote_average(double vote_average) {
            this.vote_average = vote_average;
        }

        @Override
        public String toString() {
            return "results{" +
                    "poster_path='" + poster_path + '\'' +
                    ", overview='" + overview + '\'' +
                    ", release_date='" + release_date + '\'' +
                    ", id=" + id +
                    ", original_title='" + original_title + '\'' +
                    ", original_language='" + original_language + '\'' +
                    ", title='" + title + '\'' +
                    ", backdrop_path='" + backdrop_path + '\'' +
                    ", popularity=" + popularity +
                    ", vote_count=" + vote_count +
                    ", vote_average=" + vote_average +
                    '}';
        }
    }
}
