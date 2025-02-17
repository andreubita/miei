import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Support {
    private List<Ticket> queue;

    public Support(){
        this.queue = new ArrayList<>();
    }

    public Support(List<Ticket> queue) {
        setQueue(queue);
    }

    public Support(Support support){
        setQueue(support.getQueue());
    }

    public void addTicket(Ticket ticket){
        this.queue.add(ticket.clone());
    }

    public Ticket searchTicket(String author, LocalDateTime time){
        for(Ticket crt : this.queue)
            if(crt.getAuthor().equals(author)
            && crt.getStartTime().equals(time))
                return crt.clone();
        return null;
    }

    public int searchTicketIndex(Ticket ticket){
        for(int i = 0; i < this.queue.size(); i++)
            if(this.queue.get(i).equals(ticket))
                return i;
        return -1;
    }

    public void resolveTicket(Ticket ticket, String handler, String info){
        int search = searchTicketIndex(ticket);
        ticket.setHandler(handler);
        ticket.setResponse(info);
        ticket.setDoneTime(LocalDateTime.now());
        if(search != -1) this.queue.set(search, ticket.clone());
        else this.queue.add(ticket.clone());
    }

    public void resolveTicket(Ticket ticket, String handler, String info, LocalDateTime time){
        int search = searchTicketIndex(ticket);
        ticket.setHandler(handler);
        ticket.setResponse(info);
        ticket.setDoneTime(time);
        if(search != -1) this.queue.set(search, ticket.clone());
        else this.queue.add(ticket.clone());
    }

    public List<Ticket> resolved(){
        List<Ticket> resolved = new ArrayList<>();
        for(Ticket crt : this.queue)
            if(crt.getHandler() != null
            && crt.getResponse() != null
            && crt.getDoneTime() != null)
                resolved.add(crt.clone());
        return resolved;
    }

    public List<Ticket> resolved(LocalDateTime start, LocalDateTime end){
        List<Ticket> resolved = new ArrayList<>(resolved());
        for(Ticket crt : resolved)
            if(start.isBefore(crt.getDoneTime())
            && end.isAfter(crt.getDoneTime()))
                resolved.add(crt.clone());
        return resolved;
    }

    public String topHandler(){
        HashMap<String,Integer> tops = new HashMap<>();
        for(Ticket crt : this.queue)
            if(!tops.containsKey(crt.getHandler())) tops.put(crt.getHandler(), 1);
            else tops.put(crt.getHandler(), tops.get(crt.getHandler()) + 1);
        return Collections.max(tops.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    public double doneTime(Ticket ticket){
        if(ticket.getDoneTime() == null)
            return -1;
        return Duration.between(ticket.getStartTime(), ticket.getDoneTime()).toMinutes();
    }

    public double averageDoneTime(){
        return averageDoneTime(LocalDateTime.MIN, LocalDateTime.MAX);
    }

    public double averageDoneTime(LocalDateTime start, LocalDateTime end){
        double total = 0;
        int n = 0;
        for(Ticket crt : this.queue){
            if(crt.getDoneTime() != null
            && start.isBefore(crt.getDoneTime())
            && end.isAfter(crt.getDoneTime())){
                total += doneTime(crt.clone());
                n++;
            }
        }
        return total / n;
    }

    public Ticket longestDoneTime(){
        return longestDoneTime(LocalDateTime.MIN, LocalDateTime.MAX);
    }

    public Ticket longestDoneTime(LocalDateTime start, LocalDateTime end){
        if(this.queue.size() == 0) return null;
        Ticket longest = null;
        for (Ticket ticket : this.queue)
            if (start.isBefore(ticket.getDoneTime())
            && end.isAfter(ticket.getDoneTime())
            && (longest == null || doneTime(ticket) > doneTime(longest)))
                longest = ticket.clone();
        return longest;
    }

    public Ticket shortestDoneTime(){
        return shortestDoneTime(LocalDateTime.MIN, LocalDateTime.MAX);
    }

    public Ticket shortestDoneTime(LocalDateTime start, LocalDateTime end){
        if(this.queue.size() == 0) return null;
        Ticket longest = null;
        for (Ticket ticket : this.queue)
            if (start.isBefore(ticket.getDoneTime())
            && end.isAfter(ticket.getDoneTime())
            && (longest == null || doneTime(ticket) < doneTime(longest)))
                longest = ticket.clone();
        return longest;
    }

    public List<Ticket> getQueue() {
        List<Ticket> newArr = new ArrayList<>();
        for(Ticket crt : this.queue)
            newArr.add(crt.clone());
        return newArr;
    }

    public void setQueue(List<Ticket> queue) {
        List<Ticket> newArr = new ArrayList<>();
        for(Ticket crt : this.queue)
            newArr.add(crt.clone());
        this.queue = newArr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Support support = (Support) o;

        return getQueue() != null ? getQueue().equals(support.getQueue()) : support.getQueue() == null;
    }

    @Override
    public int hashCode() {
        return getQueue() != null ? getQueue().hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Support{");
        sb.append("queue=").append(queue);
        sb.append('}');
        return sb.toString();
    }

    @Override
    protected Support clone() {
        return new Support(this);
    }
}
