package weixin.util;

public class StopWatch {  
    
    private long startTime;  
    private long endTime;  
// 一次hessian请求耗时  
    private long elapsedTime;  
//成功失败  
    private boolean status;  
      
    public StopWatch(){  
        this.startTime = 0L;  
        this.endTime = 0L;  
        this.elapsedTime = 0L;  
        this.status = false;  
    }  
      
    public long getStartTime() {  
        return startTime;  
    }  
    public void setStartTime(long startTime) {  
        this.startTime = startTime;  
    }  
    public long getEndTime() {  
        return endTime;  
    }  
    public void setEndTime(long endTime) {  
        this.endTime = endTime;  
    }  
    public boolean isStatus() {  
        return status;  
    }  
    public void setStatus(boolean status) {  
        this.status = status;  
    }  
    public long getElapsedTime() {  
        return elapsedTime;  
    }  
    public void setElapsedTime(long elapsedTime) {  
        this.elapsedTime = elapsedTime;  
    }  
  
}  