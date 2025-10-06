package app.springdev.objectmapping.dto;

public enum Company {

    SKT("SKT"),
    KT("KT"),
    LGU("LGU");

    private String code;

    private Company(String code) {
        this.code = code;
    }


    @Override
    public String toString()
    {
        return this.code;
    }

}
