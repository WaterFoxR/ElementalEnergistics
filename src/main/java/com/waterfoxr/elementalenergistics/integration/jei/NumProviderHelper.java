package com.waterfoxr.elementalenergistics.integration.jei;

import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

import java.util.List;

public class NumProviderHelper {

    public static int getNum(IngredientElementType element) {
        // 使用内部类进行转换
        return ((INumProvider) (Object) element).getNum();
    }

    public static void setNum(IngredientElementType element, int num) {
        ((INumProvider) (Object) element).setNum(num);
    }

    public int getGaugeValue(int amount){
        return (int)Math.log10((double)amount) - 1;
    }

    /**
     * 替代原始方法{@link IngredientElementType#all(int)}，因为其真实配方的元素消耗量会被模糊成少量、中量、大量、巨量从而丢失
     * @param amount 原经过{@link sirttas.elementalcraft.interaction.jei.category.AbstractInventoryRecipeCategory#getGaugeValue(int)}级数模糊的输入
     * @param elementAmount 真实的元素消耗量
     * @return 返回带有真实的元素消耗量的
     */
    public static List<IngredientElementType> allWithNum(int amount, int elementAmount) {
        return ElementType.ALL_VALID.stream().map((type) -> {
            IngredientElementType IType =  new IngredientElementType(type, amount);
            NumProviderHelper.setNum(IType,elementAmount);
            return IType;
        }).toList();
    }

}