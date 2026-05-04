package org.psyrioty.magicCostume.Objects.Animations;

import org.bukkit.event.EventHandler;
import org.psyrioty.magicCostume.Objects.Player.Condition;

import java.util.List;

//хуйнюшка для переходов анимаций, нужно для масштабируемости
public class AnimationState {
    //Animation animation; //анимация стэйта
    String name; //должно быть равным названию анимации
    List<EventHandler> eventHandlers; //события, которые вызовут анимацию
    List<Condition> conditions; //условия, которые вызовут анимацию

    public AnimationState(
            String name,
            List<EventHandler> eventHandlers,
            List<Condition> conditions
    ){
        this.name = name;
        this.eventHandlers = eventHandlers;
        this.conditions = conditions;
    }
}
