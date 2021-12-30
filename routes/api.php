<?php

use App\Http\Controllers\Api\AuthController;
use App\Http\Controllers\Api\BabyController;
use App\Http\Controllers\Api\NewsController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

Route::namespace('Api')->group(function () {
    Route::prefix('auth')->group(function () {
        Route::post('register', [AuthController::class, 'register']);
        Route::post('login', [AuthController::class, 'login']);
        Route::post('forgot', [AuthController::class, 'forgot']);
    });
    Route::group(['prefix' => 'auth', 'middleware' => 'auth:sanctum'], function () {
        Route::get('user', [AuthController::class, 'user']);
        Route::post('profile', [AuthController::class, 'profile']);
        Route::get('logout', [AuthController::class, 'logout']);
        Route::post('user', [AuthController::class, 'update']);
        Route::post('admin', [AuthController::class, 'admin']);
        Route::get('home', [AuthController::class, 'home']);
        // Route::post('register', [AuthController::class, 'register']);
    });
    Route::group(['prefix' => 'babies', 'middleware' => 'auth:sanctum'], function () {
        Route::post('/', [BabyController::class, 'store']);
        Route::get('/', [BabyController::class, 'index']);
        Route::get('admin', [BabyController::class, 'admin']);
        Route::post('/{id}', [BabyController::class, 'update']);
        Route::post('riject/{id}', [BabyController::class, 'riject']);
        Route::post('pdf/{id}', [BabyController::class, 'pdf']);
        Route::get('status', [BabyController::class, 'status']);
        Route::get('babies', [BabyController::class, 'babies']);
    });
    Route::group(['prefix' => 'news', 'middleware' => 'auth:sanctum'], function () {
        Route::get('/', [NewsController::class, 'index']);
    });
});