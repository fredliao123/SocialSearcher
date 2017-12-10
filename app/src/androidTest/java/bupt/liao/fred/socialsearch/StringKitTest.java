package bupt.liao.fred.socialsearch;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import bupt.liao.fred.socialsearch.kit.StringKit;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by Fred.Liao on 2017/12/10.
 * Email:fredliaobupt@qq.com
 * Description:
 */
@RunWith(AndroidJUnit4.class)
public class StringKitTest {
    @Test
    public void stringKitTest_ExtractLinks_ReturnLinkArray(){
        //Given
        String[] linkArray = {"https://en.wikipedia.org/wiki/"};
        String testString = "wiki's website is https://en.wikipedia.org/wiki/";
        //When
        String[] resultArray = StringKit.getInstance().extractLinks(testString);
        //Then
        assertThat(resultArray[0].equals(linkArray[0]));
    }
}
