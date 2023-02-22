public class Fuzzy {
    Double[] speed = new Double[5]; //0=vs, 1=s, 2=m, 3=f, 4=vf
    Double[] distance = new Double[5];
    Double[] open = new Double[5];

    public Fuzzy(Double speed, Double distance) {

        Fuzzifier(speed*50,distance);
        Rule();
        double a = Defuzzification()-(0.9 * speed / distance);
        System.out.println("Distance : " + distance + " m  Speed : " + speed + " m/s   Openspeed : " + Defuzzification() + "m/s  " +
                "real speed : " + 0.9 * speed / distance);
    }

    public void Fuzzifier(Double x, Double y){
        for (int i = 0; i < 5; i++) {
            speed[i] = 0.0;
            distance[i] = 0.0;
            open[i] = 0.0;
            switch (i) {
                case 0 -> {
                    if (0 <= x && x <= 10) speed[0] = (10 - x) / 10;
                    if (0 <= y && y <= 1) distance[0] = 1 - y;
                }
                case 1 -> {
                    if (0 <= x && x <= 10) speed[1] = x / 10;
                    else if (10 <= x && x <= 20) speed[1] = (20 - x) / 10;
                    if (0 <= y && y <= 1) distance[1] = y;
                    else if (1 <= y && y <= 2) distance[1] = 2 - y;
                }
                case 2 -> {
                    if (10 <= x && x <= 20) speed[2] = (x - 10) / 10;
                    else if (20 <= x && x <= 30) speed[2] = (30 - x) / 10;
                    if (1.5 <= y && y <= 2.5) distance[2] = y - 1.5;
                    else if (2.5 <= y && y <= 3.5) distance[2] = 3.5 - y;
                }
                case 3 -> {
                    if (25 <= x && x <= 35) speed[3] = (x - 25) / 10;
                    else if (35 <= x && x <= 45) speed[3] = (45 - x) / 10;
                    if (2.5 <= y && y <= 3.5) distance[3] = y - 2.5;
                    else if (3.5 <= y && y <= 4.5) distance[3] = 4.5 - y;
                }
                case 4 -> {
                    if (40 <= x && x <= 50) speed[4] = (x - 40) / 10;
                    if (4 <= y && y <= 5) distance[4] = y - 4;
                }
            }
        }
    }

    public void Rule(){
        double t;
        //speed
        boolean vs = false;
        boolean s = false;
        boolean ms = false;
        boolean f = false;
        boolean vf = false;
        if(speed[0]>0) vs=true;
        if(speed[1]>0) s=true;
        if(speed[2]>0) ms =true;
        if(speed[3]>0) f=true;
        if(speed[4]>0) vf=true;

        //distance
        boolean vc = false;
        boolean c = false;
        boolean md = false;
        boolean a = false;
        boolean ta = false;
        if(distance[0]>0) vc=true;
        if(distance[1]>0) c=true;
        if(distance[2]>0) md=true;
        if(distance[3]>0) a=true;
        if(distance[4]>0) ta=true;

        if(vs&&ta) UpdateDistance("vs",Math.min(speed[0], distance[4])); //1
        if(vs&&a)  UpdateDistance("vs",Math.min(speed[0], distance[3])); //2
        if(vs&&md)  UpdateDistance("s",Math.min(speed[0], distance[2])); //3
        if(vs&&c)  UpdateDistance("m",Math.min(speed[0], distance[1])); //4
        if(vs&&vc)  UpdateDistance("vf",Math.min(speed[0], distance[0])); //5

        if(s&&ta)  UpdateDistance("vs",Math.min(speed[1], distance[4])); //6
        if(s&&a)  UpdateDistance("vs",Math.min(speed[1], distance[3])); //7
        if(s&&md)  UpdateDistance("s",Math.min(speed[1], distance[2])); //8
        if(s&&c)  UpdateDistance("m",Math.min(speed[1], distance[1])); //9
        if(s&&vc)  UpdateDistance("vf",Math.min(speed[1], distance[0])); //10

        if(ms&&ta)  UpdateDistance("s",Math.min(speed[2], distance[4])); //11
        if(ms&&a)  UpdateDistance("s",Math.min(speed[2], distance[3])); //12
        if(ms&&md)  UpdateDistance("m",Math.min(speed[2], distance[2])); //13
        if(ms&&c)  UpdateDistance("f",Math.min(speed[2], distance[1])); //14
        if(ms&&vc)  UpdateDistance("vf",Math.min(speed[2], distance[0])); //15

        if(f&&ta)  UpdateDistance("s",Math.min(speed[3], distance[4])); //16
        if(f&&a)  UpdateDistance("m",Math.min(speed[3], distance[3])); //17
        if(f&&md)  UpdateDistance("f",Math.min(speed[3], distance[2])); //18
        if(f&&c)  UpdateDistance("vf",Math.min(speed[3], distance[1])); //19
        if(f&&vc)  UpdateDistance("vf",Math.min(speed[3], distance[0])); //20

        if(vf&&ta)  UpdateDistance("s",Math.min(speed[4], distance[4])); //21
        if(vf&&a)  UpdateDistance("m",Math.min(speed[4], distance[3])); //22
        if(vf&&md)  UpdateDistance("f",Math.min(speed[4], distance[2])); //23
        if(vf&&c)  UpdateDistance("vf",Math.min(speed[4], distance[1])); //24
        if(vf&&vc)  UpdateDistance("vf",Math.min(speed[4], distance[0])); //25
    }

    public void UpdateDistance(String type,Double t){
        switch (type) {
            case "vs" -> {
                open[0] = Math.max(Math.min(t, 1.0), open[0]);
                open[1] = Math.max(0, open[1]);
                open[2] = Math.max(0, open[2]);
                open[3] = Math.max(0, open[3]);
                open[4] = Math.max(0, open[4]);
            }
            case "s" -> {
                open[0] = Math.max(0, open[0]);
                open[1] = Math.max(Math.min(t, 1.0), open[1]);
                open[2] = Math.max(0, open[2]);
                open[3] = Math.max(0, open[3]);
                open[4] = Math.max(0, open[4]);
            }
            case "m" -> {
                open[0] = Math.max(0, open[0]);
                open[1] = Math.max(0, open[1]);
                open[2] = Math.max(Math.min(t, 1.0), open[2]);
                open[3] = Math.max(0, open[3]);
                open[4] = Math.max(0, open[4]);
            }
            case "f" -> {
                open[0] = Math.max(0, open[0]);
                open[1] = Math.max(0, open[1]);
                open[2] = Math.max(0, open[2]);
                open[3] = Math.max(Math.min(t, 1.0), open[3]);
                open[4] = Math.max(0, open[4]);
            }
            case "vf" -> {
                open[0] = Math.max(0, open[0]);
                open[1] = Math.max(0, open[1]);
                open[2] = Math.max(0, open[2]);
                open[3] = Math.max(0, open[3]);
                open[4] = Math.max(Math.min(t, 1.0), open[4]);
            }
        }
    }

    public double Defuzzification(){
        Double max = open[0];
        double output = 0.0;

        for(int i = 1; i < 5; i++){
            if(open[i]>max){
                max = open[i];
                output = i;
            }
        }
        output = switch ((int) output) {
            case 1 -> 0.2;
            case 2 -> 0.4;
            case 3 -> 0.7;
            case 4 -> 2.0;
            default -> 0.1;
        };
        return output;
    }
}