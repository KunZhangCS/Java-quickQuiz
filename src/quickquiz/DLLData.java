package quickquiz;

/**
 *
 * @author Kun
 */
public class DLLData
{
    String question;
    String number;
    
    public DLLData(String s1, String s2)
    {
        question = s1;
        number = s2;
    }

    public String getQnNumber()
    {
        return question;
    }

    public void setQnNumber(String question)
    {
        this.question = question;
    }

    public String getQnTopic()
    {
        return number;
    }

    public void setQnTopic(String number)
    {
        this.number = number;
    }
    
}
