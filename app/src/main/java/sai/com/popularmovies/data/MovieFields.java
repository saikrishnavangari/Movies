package sai.com.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by krrish on 29/11/2016.
 */

public interface MovieFields {

    //Fields for the table Movies are declared here
    @DataType(INTEGER)
    @PrimaryKey
    @AutoIncrement
    String Column_Id = "_id";
    @DataType(INTEGER)
    String Column_movieId = "movieId";

    @DataType(TEXT)
    @NotNull
    String Column_TITLE = "title";

    @DataType(INTEGER)
    @NotNull
    String Column_voteCount = "voteCount";

    @DataType(TEXT)
    @NotNull
    String Column_posterPath = "posterPath";

    @DataType(TEXT)
    @NotNull
    String Column_overview = "overview";

    @DataType(INTEGER)
    @NotNull
    String Column_popularity = "popularity";

    @DataType(INTEGER)
    @NotNull
    String Column_voteAverage = "voteAverage";

    @DataType(TEXT)
    @NotNull
    String Column_language = "language";

    @DataType(TEXT)
    @NotNull
    String Column_backdropPath = "backdropPath";

    @DataType(TEXT)
    @NotNull
    String Column_releaseDate = "releaseDate";
}
