package org.psyrioty.magicCostume.Objects.Animations;

import org.bukkit.event.EventHandler;
import org.psyrioty.magicCostume.Objects.Player.Condition;

import java.util.List;

//хуйнюшка для переходов анимаций, нужно для масштабируемости
public class AnimationState {
    Animation animation; //анимация стэйта
    List<EventHandler> eventHandlers; //события, которые вызовут анимацию
    List<Condition> conditions; //условия, которые вызовут анимацию

    public AnimationState(){

    }
}
