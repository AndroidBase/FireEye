package com.github.yoojia.fireeye.validators;

import android.text.TextUtils;

import com.github.yoojia.fireeye.Type;
import com.github.yoojia.fireeye.supports.AbstractValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * User: chenyoca@gmail.com
 * IsDate: 2014-09-02
 * Credit card
 */
class IDCardValidator extends AbstractValidator {

    public IDCardValidator(Type testType, String message){
        super(testType, message);
    }

    @Override
    public boolean isValid(String inputValue) {
        if (TextUtils.isEmpty(inputValue)) return false;
        final int length = inputValue.length();
        if (15 == length){
            try{
                return isOldCNIDCard(inputValue);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return false;
            }
        }else if (18 == length){
            System.out.println(">>> 测试18位身份证号：" + inputValue);
            return isNewCNIDCard(inputValue);
        }else {
            return false;
        }
    }

    static final int[] WEIGHT = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};

    static final char[] VALID = {'1','0','X','9','8','7','6','5','4','3','2'};

    public static boolean isNewCNIDCard(String numbers){
        int sum = 0;
        for (int i = 0; i < WEIGHT.length; i++){
            final int cell = Character.getNumericValue(numbers.charAt(i));
            sum += WEIGHT[i] * cell;
        }
        int index = sum % 11;
        return VALID[index] == numbers.charAt(17);
    }

    public static boolean isOldCNIDCard(String numbers){
        //ABCDEFYYMMDDXXX
        String abcdef = numbers.substring(0,5);
        String yymmdd = numbers.substring(6,11);
        String xxx = numbers.substring(12,14);
        boolean aPass = abcdef.equals(String.valueOf(Integer.parseInt(abcdef)));
        boolean yPass = true;
        try {
            new SimpleDateFormat("YYMMdd").parse(yymmdd);
        } catch (ParseException e) {
            e.printStackTrace();
            yPass = false;
        }
        boolean xPass = isMatched("\\d{2}[\\dxX]", xxx);
        return aPass && yPass && xPass;
    }

}
