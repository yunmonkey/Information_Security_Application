package com.tongmyung.yun.securityapplication.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.tongmyung.yun.securityapplication.model.HashMapadd
import com.tongmyung.yun.securityapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import com.tongmyung.yun.securityapplication.expansion.clear

/*지금 까지 입력한값 영어만 toByte()해주어서 P를 구했다

* 한글은 2Byte인데 어떻게 8 바이트씩 나누어서 표현?

* 영어 특수문자 는 거의 확인 다 되었음*

* 지금 EP와 IP XOR 까지 구현 완료 했음 이제 뒤에 S박스를 하는것만 남음

* 2018-11-01 S박스 구현 완료*/

//SBox 계산 하는 함수 즉 F(R,SK)에서 확장시켜주고 SBox값 찾아가는것 까지 구현 완료

//이제 스위치 하는 것 남았음

//암호화 작성완료 복호화만 남았음!

//복호화 완료 그런데 한글 안됨
class MainActivity : AppCompatActivity() {
    private val P10_Index = arrayOf(3, 5, 2, 7, 4, 10, 1, 9, 8, 6)
    private val P8_Index = arrayOf(6, 3, 7, 4, 8, 5, 10, 9)
    private val IP_Index = arrayOf(2, 6, 3, 1, 4, 8, 5, 7)
    private val EP_Index = arrayOf(7, 4, 5, 6, 5, 6, 7, 4)
    private val IP_1_Index = arrayOf(4, 1, 3, 5, 7, 2, 8, 6)
    var F_FUN = arrayOf(0, 0, 0, 0, 0, 0, 0, 0)
    private val S_Box0_row = arrayOf(1, 4) //S_BOX0 가로  상단 인덱스
    private val S_Box0_column = arrayOf(2, 3) //S_BOX0 세로 좌측 인덱스
    private val S_Box1_row = arrayOf(5, 8) //S_Box1 가로 상단 인덱스
    private val S_Box1_column = arrayOf(6, 7) //S_BOX1 세로 좌측 인덱스
    private val LEFT = arrayOf(1, 2, 3, 4)
    val P4 = arrayOf(2, 4, 3, 1)
    val switch_FromLeftToRight = arrayOf(5, 6, 7, 8) //왼쪽것을 오른쪽으로 옮기는 변수
    val switch_FromRightToLeft = arrayOf(1, 2, 3, 4) //오른쪽것을 왼쪽으로 옮기는 변수
    var P10 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0", "0", "0") //P10 값 초기화
    lateinit var subkey_Binary: String // Interger.toBinaryString 해주기위해 만들어줌
    lateinit var plainText: String //평서문을 넣어주는곳
    lateinit var plainText_Binary: String // 입력한 평서문 값을 toByte() 한 결과값을 여기다가 넣어줌 즉 P값
    var IP = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")//IP(초기 순열함수)값을  초기화 P를 토대로 다시 초기화 시켜줌
    var IP_1 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")//IP-1(최종 순열함수)값을 초기화 P를 토대로 다시 초기화
    var E_P = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")
    var K1 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0") //K1 초기화
    var K2 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0") //K2 초기화
    lateinit var security_sentence: String //암호화 시킨 문장
    lateinit var recovery_Sentence: String //복호화 시킨 문장
    lateinit var hashMap: HashMapadd
    lateinit var security_sentence_Binary: String
    lateinit var binarySBoxZeroResult: String
    lateinit var binarySBoxOneResult: String
    var SBoxResult = arrayOf("0", "0", "0", "0") //P4 결과값을 넣어주는곳
    var SBoxResultTemp = arrayOf("0", "0", "0", "0") //P4결과값을 임시로 넣어주는곳
    val SBoxIndex = arrayOf(2, 3)
    var fkResultLeft = arrayOf("0", "0", "0", "0")
    lateinit var binary_Change: String //한글이면 16비트로 바꾸어 주는것
    var ko: Boolean = false
    lateinit var security_binary_Change: String
    var charOver_TWOFIVESIX: Boolean = false
    var koArray1 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")
    var koArray2 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")
    var koArray1_Index = arrayOf(0, 1, 2, 3, 4, 5, 6, 7)
    var koArray2_Index = arrayOf(8, 9, 10, 11, 12, 13, 14, 15)
    var koArrayTwice: Boolean = false
    var security_char = 0
    var callTwice_IP_1: Boolean = false
    var koTemp = 0
    var decrytionkoArray1 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")
    var decrytionkoArray2 = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")
    var decrytionkoArray1_Index = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
    var decrytionkoArray2_Index = arrayOf(8, 9, 10, 11, 12, 13, 14, 15)
    var decrytionTwice = false
    var callTwice_decryption_IP_1 = false
    var decryptionTemp = 0
    var decryptionChar = 0
    var make_TenSix_Bit = ""
    var make_decryption_TenSix_Bit = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hashMap = HashMapadd()
        hashMap.S0_map_add()
        hashMap.S1_map_add()




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //상태바 색깔 변경 하는 것
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.parseColor("#f2f2f2")
        }
        button_security.setOnClickListener { v ->
            // 암호화 버튼 누르면 실행
            security_sentence = String()
            subkey_check()
//            P8, P10, K1, K2, P, IP, IP-1 안에 다있음
            binaryChange()

            textView_security_sentence.text = security_sentence

        }

        button_insecurity.setOnClickListener {
            recovery_Sentence = String()
            decryption()

            textView_decrytionText.text = recovery_Sentence
        }
        button_reset.setOnClickListener { v ->
            plainTextReset()
            make_TenSix_Bit = "" //중복 방지
        }
    }

    fun binaryChange() { //암호화 실행
        try {
            enable_plainText()
            subkey_Binary_make()
            P10_make()
            shift_calculate()
            K1_make()
            shift_calculate() // ?????? ?? shift ??
            shift_calculate()
            K2_make()
            input_security_key()
            plainText = input_normalKey.text.toString()
            make_P()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun subkey_Binary_make() {
        var subkey = input_subkey.text
        var subkey_integer = Integer.parseInt(subkey.toString())
        var subkey_binary_temp = Integer.toBinaryString(subkey_integer)
        var subkey_binary_tempToint = Integer.valueOf(subkey_binary_temp)
        subkey_Binary = String.format("%010d", subkey_binary_tempToint)
    }

    fun K1_make() { //K1 만드는 함수
        for (i in K1.indices)
            K1[i] = P10[P8_Index[i] - 1]
    }

    fun K2_make() { //K2 만드는함수
        for (i in K2.indices)
            K2[i] = P10[P8_Index[i] - 1]
    }

    fun P10_make() { //P10 만들어줌
        for (i in P10.indices)
            P10[i] = subkey_Binary[P10_Index[i] - 1].toString()
    }

    fun shift_calculate() { //옆으로 비트 옮겨줌
        var temp = P10[0]
        for (i in 1..5) {
            P10[i - 1] = P10[i]
        }
        P10[4] = temp
        temp = P10[5]
        for (i in 6..9) {
            P10[i - 1] = P10[i]
        }
        P10[9] = temp
    }

    fun input_security_key() { //textView 결과값을 넣어주는 함수
        var P10_list = ArrayList<String>()
        var K1_list = ArrayList<String>()
        var K2_list = ArrayList<String>()
        for (i in P10.indices)
            P10_list.add(P10[i])

        for (j in K1.indices)
            K1_list.add(K1[j])

        for (x in K2.indices)
            K2_list.add(K2[x])



        P10_Result.text = P10_list.toString()
        K1_Result.text = K1_list.toString()
        K2_Result.text = K2_list.toString()

    }

    fun make_P() { //안에 IP와 EP만드는것 있음
        for (i in plainText.indices) {
            if (plainText[i].toInt() < 256) {
                ko = false
                var P_integer = plainText[i].toByte() //Byte 형으로 변환
                var P_temp = Integer.toBinaryString(P_integer.toInt()) //toBinaryString 으로 형변환
                var P_temp_To_Int = Integer.valueOf(P_temp)


                plainText_Binary = String.format("%08d", P_temp_To_Int) //8자리가 안되면 0으로 채움

                make_IP() // IP만들어주는 함수
                E_P_make()//함수 fk  E_P 만듦
                exclusiveOR_EP_K1() //K1와 XOR
                S_Box_calculator() //S-Box 사용
                switchFun()//스위치
                E_P_make()//K2 만드는것
                exclusiveOR_EP_K2() //K2 XOR
                S_Box_calculator() //S-Box 사용
                security_IP_1()
            } else if (Integer.toBinaryString(plainText[i].toInt()).length == 14) { // 자음을 입력했을때 14이기 때문에 else if 걸어둠
                ko = true

                var BinaryString = Integer.toBinaryString(plainText[i].toInt())
                var binary = String.format("%16s", BinaryString)


                binary_Change = binary.replace(" ", "0")

                for (i in koArray1_Index.indices) {
                    koArray1[i] = binary_Change[koArray1_Index[i]].toString()
                    koArray2[i] = binary_Change[koArray2_Index[i]].toString()
                }
                make_IP() // IP만들어주는 함수
                E_P_make()//함수 fk  E_P 만듦
                exclusiveOR_EP_K1() //K1와 XOR
                S_Box_calculator() //S-Box 사용
                switchFun()//스위치
                E_P_make()//K2 만드는것
                exclusiveOR_EP_K2() //K2 XOR
                S_Box_calculator() //S-Box 사용
                security_IP_1()


                make_IP() // IP만들어주는 함수
                E_P_make()//함수 fk  E_P 만듦
                exclusiveOR_EP_K1() //K1와 XOR
                S_Box_calculator() //S-Box 사용
                switchFun()//스위치
                E_P_make()//K2 만드는것
                exclusiveOR_EP_K2() //K2 XOR
                S_Box_calculator() //S-Box 사용
                security_IP_1()


                koArrayTwice = false // 중복 방지
                callTwice_IP_1 = false //중복 방지
                make_TenSix_Bit = ""
            } else { //한글 들어왔을때 else 문


                ko = true

                var BinaryString = Integer.toBinaryString(plainText!![i].toInt())
                var binary = String.format("%16s", BinaryString)



                binary_Change = binary.replace(" ", "0")

                for (i in koArray1_Index.indices) {
                    koArray1[i] = binary_Change[koArray1_Index[i]].toString()
                    koArray2[i] = binary_Change[koArray2_Index[i]].toString()
                }

                make_IP() // IP만들어주는 함수
                E_P_make()//함수 fk  E_P 만듦
                exclusiveOR_EP_K1() //K1와 XOR
                S_Box_calculator() //S-Box 사용
                switchFun()//스위치
                E_P_make()//K2 만드는것
                exclusiveOR_EP_K2() //K2 XOR
                S_Box_calculator() //S-Box 사용
                security_IP_1()



                make_IP() // IP만들어주는 함수
                E_P_make()//함수 fk  E_P 만듦
                exclusiveOR_EP_K1() //K1와 XOR
                S_Box_calculator() //S-Box 사용
                switchFun()//스위치
                E_P_make()//K2 만드는것
                exclusiveOR_EP_K2() //K2 XOR
                S_Box_calculator() //S-Box 사용
                security_IP_1()

                koArrayTwice = false // 중복 방지
                callTwice_IP_1 = false //중복 방지
                make_TenSix_Bit = "" //중복 방지
            }
        }
    }

    fun make_IP() { //IP 만드는 함수
        var IP_list = ArrayList<String>()
        if (ko) {
            for (i in IP.indices) {
                IP[i] = koArray1[IP_Index[i] - 1]
                IP_list.add(IP[i])
            }
            if (koArrayTwice) {
                IP_list.clear()
                for (i in IP.indices) {
                    IP[i] = koArray2[IP_Index[i] - 1]
                    IP_list.add(IP[i])
                }

            }
            koArrayTwice = true
        } else {
            for (i in IP.indices) {
                IP[i] = plainText_Binary[IP_Index[i] - 1].toString()
                IP_list.add(IP[i])
            }
        }
        IP_Result.text = IP_list.toString()
    }

    fun subkey_check() {
        fun reset_input_subkey_Button() =
                with(input_subkey) {
                    text.clear()
                    isEnabled = true
                }

        fun reset_input_normal_key_Button() =
                with(input_normalKey) {
                    text.clear()
                    isEnabled = true
                }
        try {
            var range = input_subkey.text
            var range_Integer = Integer.parseInt(range.toString())

            if (range_Integer > 1023 || range_Integer < 0) {
//            키값 입력 잘못 됬을시 알림
                var alertDialog = AlertDialog.Builder(this)
                with(alertDialog) {
                    setTitle("키 값 재입력")
                    setIcon(R.drawable.danger_icon)
                    setPositiveButton("확인") {   dialog, which ->
                        reset_input_subkey_Button()
                        reset_input_normal_key_Button()
                    }
                    setNegativeButton("취소") { dialog, which ->
                        dialog.dismiss()
                    }
                    show()
                }
            }
        }catch (e: Exception){
            AlertDialog.Builder(this)?.let { with(it) {
                setMessage("공백불가")
                setIcon(R.drawable.danger_icon)
                setPositiveButton("확인") {
                    dialog, which -> reset_input_subkey_Button()
                    reset_input_normal_key_Button()
                    textView_security_sentence.clear("암호화문장")
                    textView_decrytionText.clear("복호화 문장")
                }
                setNegativeButton("취소") {
                    dialog, which -> dialog.dismiss()
                }
                show() }
            }
        }
    }


    //    사용못하게 막아두는 함수
    fun enable_plainText() {
        input_subkey.isEnabled = false
        input_normalKey.isEnabled = false
    }

    //초기화 시키는 함수
    fun plainTextReset() {
        input_subkey.isEnabled = true
        input_normalKey.isEnabled = true
        input_subkey.text.clear()
        input_normalKey.text.clear()
        P10_Result.text = "RESULT"
        K1_Result.text = "RESULT"
        K2_Result.text = "RESULT"
        IP_Result.text = "RESULT"
        SW_Result.text = "RESULT"
        IP_1_Result.text = "RESULT"
        Decription_IP_Result.text = "RESULT"
        Decription_IP_1_Result.text = "RESULT"
        Decription_SW_Result.text = "RESULT"
        textView_security_sentence.text = "암호화문장"
        textView_decrytionText.text = "복호화문장"

        Toast.makeText(this, "초기화 완료", Toast.LENGTH_SHORT).show()
    }

    fun E_P_make() { //EP ????? ??
        for (i in E_P.indices)
            E_P[i] = IP[EP_Index[i]]
    }

    fun exclusiveOR_EP_K1() { //?? F()?? EP? K1 XOR ?? ??
        for (i in E_P.indices) {
            if (E_P[i] == K1[i])
                F_FUN[i] = 0
            else
                F_FUN[i] = 1
        }
    }

    fun exclusiveOR_EP_K2() {
        for (i in E_P.indices) {
            if (E_P[i] == K2[i])
                F_FUN[i] = 0
            else
                F_FUN[i] = 1
        }
    }

    fun S_Box_calculator() { //SBox 계산 하는 함수 즉 F(R,SK)에서 확장시켜주고 SBox값 찾아가는것 까지 구현 완료
        var s_Box_Zero_row = arrayOf(0, 0) //에스 박스0  행
        var s_Box_Zero_column = arrayOf(0, 0) //에스박스 0 열
        var s_Box_One_row = arrayOf(0, 0)//에스박스1 행
        var s_Box_One_column = arrayOf(0, 0)//에스박스1 열
        for (i in s_Box_Zero_column.indices) {
            s_Box_Zero_row[i] = F_FUN[S_Box0_row[i] - 1]
            s_Box_Zero_column[i] = F_FUN[S_Box0_column[i] - 1]
            s_Box_One_row[i] = F_FUN[S_Box1_row[i] - 1]
            s_Box_One_column[i] = F_FUN[S_Box1_column[i] - 1]
        }
        var s_Box_ZeroRowResult = (s_Box_Zero_row[0] * 2) + (s_Box_Zero_row[1] * 1)
        var s_Box_ZeroColumnResult = (s_Box_Zero_column[0] * 2) + (s_Box_Zero_column[1] * 1)
        var s_BoxOneRowResult = (s_Box_One_row[0] * 2) + (s_Box_One_row[1] * 1)
        var s_BoxOneColumnResult = (s_Box_One_column[0] * 2) + (s_Box_One_column[1] * 1)


        var rowResult_SBoxZero = hashMap?.S0_map?.get(s_Box_ZeroRowResult)
        var s_ZeroBoxResult = rowResult_SBoxZero?.get(s_Box_ZeroColumnResult)
        var rowReulst_SBoxOne = hashMap?.S1_map?.get(s_BoxOneRowResult)
        var s_OneBoxResult = rowReulst_SBoxOne?.get(s_BoxOneColumnResult)

        var s_ZeroBoxResultToBinary = Integer.toBinaryString(s_ZeroBoxResult!!)
        var s_OneBoxResultToBinary = Integer.toBinaryString(s_OneBoxResult!!)

        var s_ZeroBoxResultToBinaryInt = Integer.valueOf(s_ZeroBoxResultToBinary)
        var s_OneBoxResultToBinaryInt = Integer.valueOf(s_OneBoxResultToBinary)


        binarySBoxZeroResult = String.format("%02d", s_ZeroBoxResultToBinaryInt)
        binarySBoxOneResult = String.format("%02d", s_OneBoxResultToBinaryInt)

        SBoxResultInput()
        leftIPXorFfun()
    }

    fun SBoxResultInput() { //SBox ??? ???? ??
        for (i in binarySBoxZeroResult?.indices!!) {
            SBoxResultTemp[i] = binarySBoxZeroResult!![i].toString()
            SBoxResultTemp[SBoxIndex[i]] = binarySBoxOneResult!![i].toString()
        }

        for (i in SBoxResult.indices) //P4 ???? ?? ??
            SBoxResult[i] = SBoxResultTemp[P4[i] - 1]

    }

    fun leftIPXorFfun() {
        for (i in SBoxResult.indices) {
            if (SBoxResult[i] == IP[LEFT[i] - 1])
                fkResultLeft[i] = "0"
            else
                fkResultLeft[i] = "1"
        }
        for (i in fkResultLeft.indices)
            IP[i] = fkResultLeft[i]
    }

    fun switchFun() {
        var tempLeftFour = arrayOf("0", "0", "0", "0") //왼쪽것을 오른쪽으로 옮기기위해서 변수 하나 만들어줌
        var tempRightFour = arrayOf("0", "0", "0", "0")
        var switchIP = ArrayList<String>()
        var decrytionSwithIP = ArrayList<String>()
        var before_decrytion_IP = arrayOf("0", "0", "0", "0", "0", "0", "0", "0")
        if (ko) {
            for (i in tempLeftFour.indices) {
                tempLeftFour[i] = IP[switch_FromRightToLeft[i] - 1] // 이것하면 왼쪽것 4개가 temp 에 들어감
                tempRightFour[i] = IP[switch_FromLeftToRight[i] - 1] //오른쪽것 4개 들어감
                before_decrytion_IP[switch_FromRightToLeft[i] - 1] = tempRightFour[i]
                before_decrytion_IP[switch_FromLeftToRight[i] - 1] = tempLeftFour[i]
                IP[switch_FromRightToLeft[i] - 1] = tempRightFour[i]
                IP[switch_FromLeftToRight[i] - 1] = tempLeftFour[i]
            }


            for (j in IP.indices) {
                switchIP.add(before_decrytion_IP[j])
                decrytionSwithIP.add(IP[j])
            }
            SW_Result.text = switchIP.toString()
            Decription_SW_Result.text = decrytionSwithIP.toString()
        } else {
            for (i in tempLeftFour.indices) {
                tempLeftFour[i] = IP[switch_FromRightToLeft[i] - 1] // 이것하면 왼쪽것 4개가 temp 에 들어감
                tempRightFour[i] = IP[switch_FromLeftToRight[i] - 1] //오른쪽것 4개 들어감
                before_decrytion_IP[switch_FromRightToLeft[i] - 1] = tempRightFour[i]
                before_decrytion_IP[switch_FromLeftToRight[i] - 1] = tempLeftFour[i]
                IP[switch_FromRightToLeft[i] - 1] = tempRightFour[i]
                IP[switch_FromLeftToRight[i] - 1] = tempLeftFour[i]
            }

            for (j in IP.indices) {
                switchIP.add(before_decrytion_IP[j])
                decrytionSwithIP.add(IP[j])
            }
            SW_Result.text = switchIP.toString()
            Decription_SW_Result.text = decrytionSwithIP.toString()
        }


    }


    fun security_IP_1() {
        var IP_1_List = ArrayList<String>()
        if (ko) {
            for (i in IP_1_Index.indices) {
                IP_1[i] = IP[IP_1_Index[i] - 1]
                IP_1_List.add(IP_1[i])
            }

            var str = (IP_1[0] + IP_1[1] + IP_1[2] + IP_1[3] + IP_1[4] + IP_1[5] + IP_1[6] + IP_1[7]).trim()


            make_TenSix_Bit += str

            security_char = Integer.parseInt(make_TenSix_Bit, 2)//첫번째 매개변수 string 두번째 n진수


            if (callTwice_IP_1) {
                koTemp += security_char
                security_sentence += (koTemp.toChar().toString())


                IP_1_Result.text = IP_1_List.toString()
                koTemp = 0
            }
            callTwice_IP_1 = true
        } else {
            for (i in IP_1_Index.indices) {
                IP_1[i] = IP[IP_1_Index[i] - 1]
                IP_1_List.add(IP_1[i])
            }

            var str = (IP_1[0] + IP_1[1] + IP_1[2] + IP_1[3] + IP_1[4] + IP_1[5] + IP_1[6] + IP_1[7]).trim()
            var security_char = Integer.parseInt(str, 2)  //첫번째 매개변수 string 두번째 n진수

            security_sentence += (security_char.toChar().toString())
            IP_1_Result.text = IP_1_List.toString()
        }

    }

    fun decrytion_IP_1() {
        var decryption_IP_1_List = ArrayList<String>()
        if (charOver_TWOFIVESIX) {
            for (i in IP_1_Index.indices) {
                IP_1[i] = IP[IP_1_Index[i] - 1]
                decryption_IP_1_List.add(IP_1[i])
            }

            var str = (IP_1[0] + IP_1[1] + IP_1[2] + IP_1[3] + IP_1[4] + IP_1[5] + IP_1[6] + IP_1[7]).trim()
            make_decryption_TenSix_Bit += str


            decryptionChar = Integer.parseInt(make_decryption_TenSix_Bit, 2)  //첫번째 매개변수 string 두번째 n진수


            if (callTwice_decryption_IP_1) {
                decryptionTemp += decryptionChar


                recovery_Sentence += (decryptionTemp.toChar().toString())
                Decription_IP_1_Result.text = decryption_IP_1_List.toString()
                decryptionTemp = 0
            }
            callTwice_decryption_IP_1 = true

        } else {

            for (i in IP_1_Index.indices) {
                IP_1[i] = IP[IP_1_Index[i] - 1]
                decryption_IP_1_List.add(IP_1[i])
            }

            var str = (IP_1[0] + IP_1[1] + IP_1[2] + IP_1[3] + IP_1[4] + IP_1[5] + IP_1[6] + IP_1[7]).trim()

            var security_char = Integer.parseInt(str, 2)  //첫번째 매개변수 string 두번째 n진수

            recovery_Sentence += (security_char.toChar().toString())
            Decription_IP_1_Result.text = decryption_IP_1_List.toString()
        }

    }

    fun make_decryption_IP() {
        var decryptionIP_List = ArrayList<String>()
        if (charOver_TWOFIVESIX) {
            for (i in IP.indices) {
                IP[i] = decrytionkoArray1[IP_Index[i] - 1]
                decryptionIP_List.add(IP[i])
            }
            if (decrytionTwice) {
                decryptionIP_List.clear()
                for (i in IP.indices) {
                    IP[i] = decrytionkoArray2[IP_Index[i] - 1]
                    decryptionIP_List.add(IP[i])
                }
                Decription_IP_Result.text = decryptionIP_List.toString()
            }
            Decription_IP_Result.text = decryptionIP_List.toString()
            decrytionTwice = true
        } else {
            for (i in IP.indices) {
                IP[i] = security_sentence_Binary[IP_Index[i] - 1].toString()
                decryptionIP_List.add(IP[i])
            }
            Decription_IP_Result.text = decryptionIP_List.toString()
        }

    }

    fun decryption() { //복호화
        try {
            for (i in plainText.indices) {
                if (security_sentence[i].toInt() < 256) {
                    var security_sentence_To_Byte = security_sentence[i].toInt() // 문장 인트형으로 바꿈
                    var security_sentence_toBinaryString = Integer.toBinaryString(security_sentence_To_Byte) //2진수 표현
                    var security_To_Int = Integer.valueOf(security_sentence_toBinaryString) //2진수 표현을 다시 인트형

                    security_sentence_Binary = String.format("%08d", security_To_Int) //8자리 아니면 8자리로 맞춤

                    make_decryption_IP()
                    E_P_make()//?? fk ??? E_P ?? ????
                    exclusiveOR_EP_K2()
                    S_Box_calculator()
                    switchFun()
                    E_P_make()//??? K1? ?? ??
                    exclusiveOR_EP_K1()
                    S_Box_calculator()
                    decrytion_IP_1()

                } else if (Integer.toBinaryString(security_sentence[i].toInt()).length == 14) {

                    charOver_TWOFIVESIX = true
                    var BinaryString = Integer.toBinaryString(textView_security_sentence.text[i].toInt())
                    var binary = String.format("%16s", BinaryString)



                    security_binary_Change = binary.replace(" ", "0")

                    for (i in koArray1_Index.indices) {
                        decrytionkoArray1[i] = security_binary_Change[decrytionkoArray1_Index[i]].toString()
                        decrytionkoArray2[i] = security_binary_Change[decrytionkoArray2_Index[i]].toString()
                    }
                    make_decryption_IP()
                    E_P_make()//?? fk ??? E_P ?? ????
                    exclusiveOR_EP_K2()
                    S_Box_calculator()
                    switchFun()
                    E_P_make()//??? K1? ?? ??
                    exclusiveOR_EP_K1()
                    S_Box_calculator()
                    decrytion_IP_1()



                    make_decryption_IP()
                    E_P_make()//?? fk ??? E_P ?? ????
                    exclusiveOR_EP_K2()
                    S_Box_calculator()
                    switchFun()
                    E_P_make()//??? K1? ?? ??
                    exclusiveOR_EP_K1()
                    S_Box_calculator()
                    decrytion_IP_1()

                    decrytionTwice = false //중복 방지
                    charOver_TWOFIVESIX = false //중복 방지
                    callTwice_decryption_IP_1 = false //중복방지
                    make_decryption_TenSix_Bit = ""
                } else {
                    charOver_TWOFIVESIX = true
                    var BinaryString = Integer.toBinaryString(textView_security_sentence.text!![i].toInt())
                    var binary = String.format("%16s", BinaryString)



                    security_binary_Change = binary.replace(" ", "0")

                    for (i in koArray1_Index.indices) {
                        decrytionkoArray1[i] = security_binary_Change[decrytionkoArray1_Index[i]].toString()
                        decrytionkoArray2[i] = security_binary_Change[decrytionkoArray2_Index[i]].toString()
                    }

                    make_decryption_IP()
                    E_P_make()//?? fk ??? E_P ?? ????
                    exclusiveOR_EP_K2()
                    S_Box_calculator()
                    switchFun()
                    E_P_make()//??? K1? ?? ??
                    exclusiveOR_EP_K1()
                    S_Box_calculator()
                    decrytion_IP_1()



                    make_decryption_IP()
                    E_P_make()//?? fk ??? E_P ?? ????
                    exclusiveOR_EP_K2()
                    S_Box_calculator()
                    switchFun()
                    E_P_make()//??? K1? ?? ??
                    exclusiveOR_EP_K1()
                    S_Box_calculator()
                    decrytion_IP_1()

                    decrytionTwice = false //중복 방지
                    charOver_TWOFIVESIX = false //중복 방지
                    callTwice_decryption_IP_1 = false // 중복방지
                    make_decryption_TenSix_Bit = ""
                }
            }
        }catch (e: Exception){
            AlertDialog.Builder(this)?.let { with(it) {
                setTitle("공백불가")
                setMessage("""공백은 불가입니다.
                    |확인을 누르시면 초기화 됩니다.
                """.trimMargin())
                setIcon(R.drawable.danger_icon)
                setNegativeButton("취소") {
                        dialog, which -> dialog.dismiss()
                    }
                setPositiveButton("확인") {
                    dialog, which ->
                        input_subkey.text.clear()
                        input_normalKey.text.clear()
                        textView_security_sentence.text = "암호화문장"
                        textView_decrytionText.text = "복호화문장"
                        input_subkey.isEnabled = true
                        input_normalKey.isEnabled = true
                    }
                show()
                }
            }
        }

    }
}
