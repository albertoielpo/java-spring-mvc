package net.ielpo.javaspringmvc.dto;

/**
 * @author Alberto Ielpo
 */
public class Todos {
    public Long userId;
    public Long id;
    public String title;
    public Boolean completed;

    public Todos() {
    }

    public Todos(Long userId, Long id, String title, Boolean completed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

}
