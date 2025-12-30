package com.waterfoxr.elementalenergistics.integration.jei;

import appeng.api.integrations.jei.IngredientConverter;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import com.waterfoxr.elementalenergistics.me.api.stacks.ElementKey;
import mezz.jei.api.ingredients.IIngredientType;
import sirttas.elementalcraft.interaction.jei.ingredient.element.IngredientElementType;

public record ElementIngredientConverter(IIngredientType<IngredientElementType> type)
        implements IngredientConverter<IngredientElementType> {


    // 使得JEI配方的“+”号生效
    @Override
    public IIngredientType<IngredientElementType> getIngredientType() {
        return IngredientElementType.TYPE;
    }

    @Override
    public IngredientElementType getIngredientFromStack(GenericStack stack) {
//        if(stack.what() instanceof ElementKey){
//            ElementType eType = ((ElementKey) stack.what()).getElementType();
//            int fakerAmount = (int) Math.log10(stack.amount()) - 1;
//            int num = Mth.clamp(fakerAmount,1,4);
//            return IngredientElementType.all().get(0);
//            IngredientElementType elementType =  new IngredientElementType(eType, num);
//            NumProviderHelper.setNum(elementType,1);
//            return elementType;
//        }
        return null;
    }


    // 使得JEI“+”号转移元素栈 已知问题：因原模组的配方元素消耗量被模糊表示成少量、中量、大量，对应范围amount的值固定为123并不是配方真实消耗的数量
    @Override
    public GenericStack getStackFromIngredient(IngredientElementType ingredient) {
        AEKey what = ElementKey.of(ingredient.elementType());
        int num =  NumProviderHelper.getNum(ingredient)<=0? ingredient.amount() : NumProviderHelper.getNum(ingredient);
        return new GenericStack(what, num);
    }


}
