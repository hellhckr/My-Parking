package codemazk.abhinay.map;



public class items {
    String id;
    String carname;
    String carnbr;
    String lati;
    String longi;
    String date;
    String time;

    public items(String id,String carname, String carnbr,String lati,String time,String date,String longi){
        this.id=id;
        this.carname=carname;
        this.carnbr = carnbr;
        this.lati=lati;
        this.longi=longi;
        this.time=time;
        this.date=date;

    }
    //Getter and Setter

    public String getIdd() {return id;}

    public void setIdd(String id){this.id=id;}

    public String getCarname() {return carname;}

    public void setCarname(String carname){this.carname=carname;}

    public String getCarnbr() {return carnbr;}

    public void setCarnbr(String carname){this.carnbr=carnbr;}

    public String getLati() {return lati;}

    public void setLati(String lati){this.lati=lati;}

    public String getLongi() {return longi;}

    public void setLongi(String longi){this.longi=longi;}

    public String getTime() {return time;}

    public void setTime(String time){this.time=time;}

    public String getDate() {return date;}

    public void setDate(String date){this.date=date;}





}
