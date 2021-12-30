<?php

function sendNotification($token, $title, $content)
{
    $curl = curl_init();

    $key_fcm = env('FCM_KEY');

    curl_setopt_array($curl, array(
        CURLOPT_URL => 'https://fcm.googleapis.com/fcm/send',
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_ENCODING => '',
        CURLOPT_MAXREDIRS => 10,
        CURLOPT_TIMEOUT => 0,
        CURLOPT_FOLLOWLOCATION => true,
        CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
        CURLOPT_CUSTOMREQUEST => 'POST',
        CURLOPT_POSTFIELDS => '{
                    "registration_ids": ["' . $token . '"],
                    "notification": {
                        "title" : "' . $title . '",
                        "body" : "' . $content . '"
                    }
                }',
        CURLOPT_HTTPHEADER => array(
            'Accept: application/json',
            'Authorization: key=' . $key_fcm . '',
            'Content-Type: application/json'
        ),
    ));

    $response = curl_exec($curl);

    curl_close($curl);
    echo $response;
}

function sendMultipleDevice($to, $title, $content)
{
    $curl = curl_init();

    $key_fcm = env('FCM_KEY');

    curl_setopt_array($curl, array(
        CURLOPT_URL => 'https://fcm.googleapis.com/fcm/send',
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_ENCODING => '',
        CURLOPT_MAXREDIRS => 10,
        CURLOPT_TIMEOUT => 0,
        CURLOPT_FOLLOWLOCATION => true,
        CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
        CURLOPT_CUSTOMREQUEST => 'POST',
        CURLOPT_POSTFIELDS => '{
                    "to": "' . $to . '",
                    "notification": {
                        "title" : "' . $title . '",
                        "body" : "' . $content . '"
                    }
                }',
        CURLOPT_HTTPHEADER => array(
            'Accept: application/json',
            'Authorization: key=' . $key_fcm . '',
            'Content-Type: application/json'
        ),
    ));

    $response = curl_exec($curl);

    curl_close($curl);
    echo $response;
}