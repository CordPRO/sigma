/*
 *  Spreadsheet by Madhawa

 
 */

package functions;

import datatypes.ArrayDataType;
import datatypes.BooleanDataType;
import datatypes.ValueDataType;
import exceptions.InvalidOperationException;
import exceptions.MissingParameterException;
import exceptions.SpreadsheetException;
import exceptions.TypeErrorException;
import solver.DataTypeElement;
import solver.ParsedElement;
import java.util.Stack;

/**
 *
 * @author 130614N
 */
/*
    returns the max of a set of values in a data-range
    the data-types of cells should be compatible with each other.
        in occasions where values are incompatible, first occuring ValueDataType will be considered base-type. its compatible types will be considered in determining max. other data types will be ignored
*/
public class Max extends AbstractFunction {

    public static final String functionName = "max";
     
    @Override
    public String getFunctionName() {
        return "max";
    }

    @Override
    public void solveReversePolishStack(Stack<DataTypeElement> stack) throws SpreadsheetException {
         //Pop the function arguements used by max
       
        if(stack.isEmpty())
            throw new MissingParameterException();
        DataTypeElement token1 = stack.pop();

        //retrieve there values
        datatypes.DataType n1 = token1.getDataType();
       
        
        datatypes.DataType n3 = n1; //pass through if only a single value is given to max
        if(n1 instanceof ArrayDataType)
        {
            //we have an array of data-types as an arguement. so function can work properly.
            ArrayDataType dataRange = (ArrayDataType)n1;
            //retrieve the vector of data given by cell-range
            datatypes.DataType[] dataVector = dataRange.asVector();
            if(dataVector.length == 0)
            {
                //empty data vector as input
                throw new InvalidOperationException();
            }
            /* Finding max */
            
            datatypes.DataType max = null;
            //loop through elements
            for(int i = 0; i < dataVector.length;i++)
            {
                if(max == null && dataVector[i] instanceof ValueDataType)
                {
                    //setup max. hence, base type is determined
                    max = dataVector[i];
                }
                try{
                    datatypes.DataType compareResult = datatypes.BasicMath.isGreaterThan(dataVector[i],max);
                    if(compareResult instanceof BooleanDataType)
                    {
                        //check comparison result
                        BooleanDataType booleanCompareResult = (BooleanDataType)compareResult;
                        if(booleanCompareResult.getValue())
                        {
                            //ith value greater than max. therefore, make it the new max
                            max = dataVector[i];
                        }

                    }
                    else throw new TypeErrorException();
                }
                catch(SpreadsheetException ex)
                {
                    //incompatible data-type. ignore the element.
                }
                
                
            }
            n3 = max;
        }
       
        
        
        
        //push the results back to the stack
        stack.push(new ParsedElement(n3));
    }
    
}
