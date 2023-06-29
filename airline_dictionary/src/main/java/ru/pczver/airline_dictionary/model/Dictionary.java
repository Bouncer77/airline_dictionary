package ru.pczver.airline_dictionary.model;

import lombok.Data;

@Data
public class Dictionary {

    Long id;
    String abbreviation;
    String originalPhrase;
    String userName;
}
