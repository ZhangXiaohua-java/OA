/**
  * Copyright 2023 json.cn 
  */
package cn.edu.huel.user.domain;
import java.util.List;

/**
 * Auto-generated: 2023-03-03 14:23:22
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Steps {

    private String instruction;
    private String orientation;
    private String step_distance;
    private Cost cost;
    private List<Cities> cities;
    public void setInstruction(String instruction) {
         this.instruction = instruction;
     }
     public String getInstruction() {
         return instruction;
     }

    public void setOrientation(String orientation) {
         this.orientation = orientation;
     }
     public String getOrientation() {
         return orientation;
     }

    public void setStep_distance(String step_distance) {
         this.step_distance = step_distance;
     }
     public String getStep_distance() {
         return step_distance;
     }

    public void setCost(Cost cost) {
         this.cost = cost;
     }
     public Cost getCost() {
         return cost;
     }

    public void setCities(List<Cities> cities) {
         this.cities = cities;
     }
     public List<Cities> getCities() {
         return cities;
     }

}