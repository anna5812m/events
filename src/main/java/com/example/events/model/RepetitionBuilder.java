package com.example.events.model;

public class RepetitionBuilder {
    private static RepetitionBuilder rep = new RepetitionBuilder();
    private String id = null;
    private String description = "";

    private RepetitionBuilder(){}

    public static RepetitionBuilder create() {
        return rep;
    }

    public RepetitionBuilder withDescription(String description){
        this.description = description;
        return rep;
    }

    public RepetitionBuilder withId(String id){
        this.id = id;
        return rep;
    }

    public Repetition build(){
        Repetition result = new Repetition(this.description);
        if(id != null)
        result.setId(id);
        return result;
    }
}
