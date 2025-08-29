package com.example.bfhl.api;

import com.example.bfhl.model.BfhlRequest;
import com.example.bfhl.model.BfhlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class BfhlController
{
    private static final String FULL_NAME = "john_doe";
    private static final String DOB_DDMMYYYY = "17091999";
    private static final String EMAIL = "john@xyz.com";
    private static final String ROLL = "ABCD123";

    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponse> handle(@RequestBody BfhlRequest request)
    {
        BfhlResponse res = new BfhlResponse();
        try
        {
            List<String> data = request.getData() == null ? List.of() : request.getData();

            List<String> odd = new ArrayList<>();
            List<String> even = new ArrayList<>();
            List<String> alphabets = new ArrayList<>();
            List<String> special = new ArrayList<>();
            List<Character> allLetters = new ArrayList<>();
            BigInteger sum = BigInteger.ZERO;

            for (String item : data)
            {
                if (item == null) continue;

                // Pure integer (supports negative/leading zeros)
                if (item.matches("^-?\\d+$"))
                {
                    BigInteger v = new BigInteger(item);
                    sum = sum.add(v);
                    if (v.abs().mod(BigInteger.TWO).equals(BigInteger.ZERO)) even.add(item);
                    else odd.add(item);
                    continue;
                }

                // Pure alphabetic token -> goes to 'alphabets' UPPER
                if (item.matches("^[A-Za-z]+$"))
                {
                    alphabets.add(item.toUpperCase());
                }

                // For concat_string (letters anywhere) + special chars (every non-alnum char)
                for (char c : item.toCharArray())
                {
                    if (Character.isLetter(c)) allLetters.add(c);
                    else if (!Character.isDigit(c)) special.add(String.valueOf(c));
                }
            }

            String concat = buildAlternatingCapsReverse(allLetters);

            res.setIsSuccess(true);
            res.setUserId(FULL_NAME.toLowerCase() + "_" + DOB_DDMMYYYY);
            res.setEmail(EMAIL);
            res.setRollNumber(ROLL);
            res.setOddNumbers(odd);
            res.setEvenNumbers(even);
            res.setAlphabets(alphabets);
            res.setSpecialCharacters(special);
            res.setSum(sum.toString());
            res.setConcatString(concat);

            return ResponseEntity.ok(res);
        }
        catch (Exception ex)
        {
            res.setIsSuccess(false);
            res.setUserId(FULL_NAME.toLowerCase() + "_" + DOB_DDMMYYYY);
            res.setEmail(EMAIL);
            res.setRollNumber(ROLL);
            res.setOddNumbers(List.of());
            res.setEvenNumbers(List.of());
            res.setAlphabets(List.of());
            res.setSpecialCharacters(List.of());
            res.setSum("0");
            res.setConcatString("");
            return ResponseEntity.ok(res);
        }
    }

    private String buildAlternatingCapsReverse(List<Character> letters)
    {
        if (letters.isEmpty()) return "";
        List<Character> copy = new ArrayList<>(letters);
        Collections.reverse(copy);
        StringBuilder sb = new StringBuilder();
        boolean upper = true;
        for (char c : copy)
        {
            char out = Character.isLetter(c) ? (upper ? Character.toUpperCase(c) : Character.toLowerCase(c)) : c;
            sb.append(out);
            upper = !upper;
        }
        return sb.toString();
    }
}