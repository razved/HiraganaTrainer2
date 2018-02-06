package com.example.android.hiraganatrainer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    String[][] hiraganaArray = new String[][]{
            {"あ", "い", "う", "え", "お"},
            {"か", "き", "く", "け", "こ"},
            {"さ", "し", "す", "せ", "そ"},
            {"た", "ち", "つ", "て", "と"},
            {"な", "に", "ぬ", "ね", "の"},
            {"は", "ひ", "ふ", "へ", "ほ"},
            {"ま", "み", "む", "め", "も"},
            {"や", "ゆ", "よ", "ん", "わ"},
            {"ら", "り", "る", "れ", "ろ"},
    };
    String[][] hiraganaTranslitArray = new String[][]{
            {"a", "i", "u", "e", "o"},
            {"ka", "ki", "ku", "ke", "ko"},
            {"sa", "shi", "su", "se", "so"},
            {"ta", "chi", "tsu", "te", "to"},
            {"na", "ni", "nu", "ne", "no"},
            {"ha", "hi", "fu", "he", "ho"},
            {"ma", "mi", "mu", "me", "mo"},
            {"ya", "yu", "yo", "n", "wa"},
            {"ra", "ri", "ru", "re", "ro"},
    };
    String[] answersArray = new String[]{};
    String hiraganaNow;
    int randRow;
    int randLine;
    int rightAnswers = 0;
    int falseAnswers = 0;
    int step = 0;
    TextView hiraganaNowQuestionText;
    TextView rightAnswersCounter;
    TextView falseAnswersCounter;
    RadioButton firstAnswer;
    RadioButton secondAnswer;
    RadioButton thirdAnswer;
    RadioGroup answersGroup;
    Button answerButton;
    LinearLayout mainLayout;
    int color = Color.TRANSPARENT;


    Random rnd = new Random(System.currentTimeMillis());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hiraganaNowQuestionText = (TextView) findViewById(R.id.hiraganaNowQuestion);
        firstAnswer = (RadioButton) findViewById(R.id.firstAnswerRadioButton);
        secondAnswer = (RadioButton) findViewById(R.id.secondAnswerRadioButton);
        thirdAnswer = (RadioButton) findViewById(R.id.thirdAnswerRadioButton);
        answersGroup = (RadioGroup) findViewById(R.id.answersGroup);
        rightAnswersCounter = (TextView) findViewById(R.id.rightAnswersCounter);
        falseAnswersCounter = (TextView) findViewById(R.id.falseAnswersCounter);
        answerButton = (Button) findViewById(R.id.answerButton);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        randRowLine();
        answersArray = generateAnswers(randLine);
        generateAndShowNewQestion();

        Drawable background = mainLayout.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.randRow), randRow);
        outState.putInt(getString(R.string.randLine), randLine);
        outState.putInt(getString(R.string.rightAnswers), rightAnswers);
        outState.putInt(getString(R.string.falseAnswers), falseAnswers);
        outState.putInt(getString(R.string.step), step);
        outState.putStringArray(getString(R.string.answersArray), answersArray);
//        outState.putString("hiraganaNow", hiraganaNow);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        randRow = savedInstanceState.getInt(getString(R.string.randRow));
        randLine = savedInstanceState.getInt(getString(R.string.randLine));
        rightAnswers = savedInstanceState.getInt(getString(R.string.rightAnswers));
        falseAnswers = savedInstanceState.getInt(getString(R.string.falseAnswers));
        step = savedInstanceState.getInt(getString(R.string.step));
        answersArray = savedInstanceState.getStringArray(getString(R.string.answersArray));
//        hiraganaNow = savedInstanceState.getString("hiraganaNow");
        showCounts();
        showQuestion();
    }

    /**
     * function set randRow and randLine global variable as  random integers
     */
    private void randRowLine() {
        randRow = rnd.nextInt(5);
        randLine = rnd.nextInt(9);

    }

    /**
     * method generate random answers from the same line as the question hiragana
     *
     * @param lineNum line of now hiragana question
     * @return
     */
    private String[] generateAnswers(int lineNum) {
        String[] answers = new String[3];
        String falseHiraganaAnswer;
        int currentAnswer;
        //decide when position will be our right answer
        int rightAnswer = rnd.nextInt(3);
        //full the array of answers

        for (int i = 0; i <= 2; i++) {
            // if it our random position of right answer, we put this
            if (i == rightAnswer) {
                answers[i] = hiraganaTranslitArray[randLine][randRow];
            } else {
                //if it isn't
                do {
                    //if this false answer doesn't the same as our true answer or previously decided one
                    //we repeat that
                    //decide the false answers
                    currentAnswer = rnd.nextInt(5);
                    falseHiraganaAnswer = hiraganaTranslitArray[lineNum][currentAnswer];
                    //check

                }
                while ((isArrayContain(answers, falseHiraganaAnswer)) || (currentAnswer == randRow));
                answers[i] = hiraganaTranslitArray[lineNum][currentAnswer];
            }
        }
//        Log.i("generateAnswers", "now array: " + Arrays.toString(answers));
        return answers;
    }

    /**
     * method check is the sring in the array or not
     *
     * @param strArray array when we find string
     * @param strKey   string that we find
     * @return true - array contain string, false - array don't contain string
     */
    private boolean isArrayContain(String[] strArray, String strKey) {
        boolean isContain = false;
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i] == strKey) {
                isContain = true;
            }
        }
        return isContain;
    }

    /**
     * method show new hiragana question and variants of answer
     * variants and show them
     */
    public void showQuestion() {
/*        randRowLine();
        answersArray = generateAnswers(randLine); */

        hiraganaNow = hiraganaArray[randLine][randRow];
        hiraganaNowQuestionText.setText(hiraganaNow);

        firstAnswer.setText(answersArray[0]);
        secondAnswer.setText(answersArray[1]);
        thirdAnswer.setText(answersArray[2]);
    }

    /**
     * method generate (by calling generateAnswers) and show (by calling showQuestion)
     * question and answers
     */
    private void generateAndShowNewQestion() {
        randRowLine();
        answersArray = generateAnswers(randLine);
        showQuestion();
    }

    /**
     * method check true choosed answer or not
     *
     * @return true - answer right, false - not
     */
    private boolean isAnswerTrue() {
        boolean isAnswer = false;
        String translitHiragana = hiraganaTranslitArray[randLine][randRow];
        if (firstAnswer.isChecked() && (answersArray[0].equals(translitHiragana))) {
            isAnswer = true;
        }
        if ((secondAnswer.isChecked()) && (answersArray[1].equals(translitHiragana))) {
            isAnswer = true;
        }
        if ((thirdAnswer.isChecked()) && (answersArray[2].equals(translitHiragana))) {
            isAnswer = true;
        }
        return isAnswer;
    }

    /**
     * method highlight right answer (if it is) by green, and false answer by red
     */
    private void markAnswer() {
        int checktId = answersGroup.getCheckedRadioButtonId();
        switch (checktId) {
            case -1:
                Toast.makeText(getApplicationContext(), "You didn't checkt any answer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.firstAnswerRadioButton:
//                Log.i("markAnswer", "FirstAnswer");
                if (isAnswerTrue()) {
                    firstAnswer.setBackgroundColor(Color.GREEN);
                    rightAnswers++;
                } else {
                    firstAnswer.setBackgroundColor(Color.RED);
                    falseAnswers++;
                }
                break;
            case R.id.secondAnswerRadioButton:
//                Log.i("markAnswer", "SecondAnswer");
                if (isAnswerTrue()) {
                    secondAnswer.setBackgroundColor(Color.GREEN);
                    rightAnswers++;
                } else {
                    secondAnswer.setBackgroundColor(Color.RED);
                    falseAnswers++;
                }
                break;
            case R.id.thirdAnswerRadioButton:
//                Log.i("markAnswer", "ThirdAnswer");
                if (isAnswerTrue()) {
                    thirdAnswer.setBackgroundColor(Color.GREEN);
                    rightAnswers++;
                } else {
                    thirdAnswer.setBackgroundColor(Color.RED);
                    falseAnswers++;
                }
                break;
            default:
                break;
        }

    }

    /**
     * method show count of true and false answers
     */
    private void showCounts() {
        rightAnswersCounter.setText("" + rightAnswers);
        falseAnswersCounter.setText("" + falseAnswers);
    }

    /**
     * method called when user touch ANSWER (or NEW GAME) button
     *
     * @param view view who called this method
     */

    public void answerButtonClick(View view) {
        markAnswer();
        showCounts();
        int delayMillis = 500;
        step++;
        // if game number came to 10
        if (step == 10) {
            if (rightAnswers > falseAnswers) {
                hiraganaNowQuestionText.setText(R.string.winMessage);
            } else {
                hiraganaNowQuestionText.setText(R.string.looseMessage);
            }

            answerButton.setText(R.string.newGameButton);
        } else if (step > 10) { //if user press NEW GAME button
            step = 0;
            answerButton.setText(R.string.answerButton);

            answersGroup.clearCheck();

            firstAnswer.setBackgroundColor(color);
            secondAnswer.setBackgroundColor(color);
            thirdAnswer.setBackgroundColor(color);

            rightAnswers = 0;
            falseAnswers = 0;
            showCounts();
            generateAndShowNewQestion();
        } else {
            //if game resume (less then 10 games)
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    answersGroup.clearCheck();
                    firstAnswer.setBackgroundColor(color);
                    secondAnswer.setBackgroundColor(color);
                    thirdAnswer.setBackgroundColor(color);
                    randRowLine();
                    answersArray = generateAnswers(randLine);
                    generateAndShowNewQestion();

                }
            }, delayMillis);
        }
    }
}
