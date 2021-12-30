<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\News;
use Illuminate\Http\Request;

class NewsController extends Controller
{
    public function index(Request $request, News $news)
    {
        $news = $news->when($request->get('q'), function ($query) use ($request){
            $query->where('title', 'like', "%{$request->get('q')}%")
                ->orWhere('body', 'like', "%{$request->get('q')}%");
        })->orderBy('id_news','desc')->paginate(50);

        return response()->json($news, 200);
    }
}