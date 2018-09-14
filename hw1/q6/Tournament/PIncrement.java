package q6.Tournament;

public class PIncrement implements Runnable {
    private Integer c;
    private volatile int incrementNumber;
    private Tournament tournament;
    private volatile int myThread;

    public PIncrement(Integer c, int incrementNumber, Tournament tournament, int myThread){
        this.c = c;
        this.incrementNumber = incrementNumber;
        this.tournament = tournament;
        this.myThread = myThread;
    }

    @Override
    public void run(){
        for(int i = 0; i < incrementNumber; i++){
            try{
                tournament.lock1(myThread);
                tournament.lock2(myThread);
                tournament.lock3(myThread);
                c++;
            }
            finally{
                tournament.unlock(myThread);
            }
        }
    }
    private static void spinupTournament(Integer c,
                                         int incrementNumber,
                                         Tournament t,
                                         PIncrement[] threadList,
                                         int listLoc) {
        threadList[listLoc] = new PIncrement(c, incrementNumber, t, listLoc);
        threadList[listLoc].run();
    }

    public static int parallelIncrement(int c, int numThreads) {
        int incrementPerThread = c / numThreads;
        int remainder = c % numThreads;

        final Tournament t = new Tournament();
        Integer tournamentInt = new Integer(0);
        PIncrement[] threadListTournament = new
                PIncrement[numThreads];
        long tournamentstart = System.nanoTime();
        for (int j = 0; j < numThreads; j++) {
            int incrementBy = (j < remainder) ? incrementPerThread + 1 :
                    incrementPerThread;
            spinupTournament(tournamentInt, incrementBy, t,
                    threadListTournament, j);
        }
        while (c != 1200000){}
        long tournamentend = System.nanoTime();
        return (int)(tournamentend - tournamentstart);
    }
}

class Tournament{
    private volatile int[] locks = new int[]{-1,-1,-1,-1,-1,-1,-1};

    private volatile boolean[] flag = new boolean[8];
    private volatile int[] victim = new int[7];
    public void lock1(int i) {
        int j;
        if(i%2 == 1){
            j = i-1;
        }
        else{
            j = i+1;
        }
        flag[i] = true;
        // I’m interested
        victim[i/2] = i;
        // you go first
        while( ( j != -1 && flag[j] && victim[i/2] == i)) {
        }; // wai	t
        locks[i/2] = i;
    }
    public void lock2(int i){
        int j;
        if((i/2)%2 == 1){
            j = locks[(i/2)-1];
        }
        else{
            j = locks[(i/2)+1];
        }
        flag[i] = true;
        victim[4+(i/4)] = i;
        while(j!=-1 && flag[j] && victim[4+(i/4)] == i){
        };
        locks[4+(i/4)] = i;
    }
    public void lock3(int i){
        int j;
        if((i/4) == 0){
            j = locks[5];
        }
        else{
            j = locks[4];
        }
        flag[i] = true;
        victim[6] = i;
        while(j!= -1 && flag[j] && victim[6] == i){
        };
        locks[6] = i;
    }
    public void unlock(int i) {
        locks[i/2] = -1;
        locks[4+(i/4)] = -1;
        locks[6] = -1;
        flag[i] = false;

        // I’m not interested
    }
}
