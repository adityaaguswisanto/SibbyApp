<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Fcm;
use App\Models\News;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Password;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    public function register(Request $request, User $user)
    {
        $request->validate([
            'name'      => 'required',
            'uname'     => 'required',
            'email'     => 'required',
            'password'  => 'required',
            'rs'        => 'required',
            'address'   => 'required',
            'hp'        => 'required',
            'role'      => 'required',
        ]);

        $user->create([
            'name'      => $request->name,
            'uname'     => $request->uname,
            'email'     => $request->email,
            'password'  => bcrypt($request->password),
            'rs'        => $request->rs,
            'address'   => $request->address,
            'hp'        => $request->hp,
            'role'      => $request->role,
        ]);

        return response()->json([
            'message' => 'New account creation successful'
        ], 201);
    }

    public function login(Request $request, User $user, Fcm $fcm)
    {
        $request->validate([
            'email'     => 'required',
            'password'  => 'required',
            'fcm'       => 'required',
        ]);

        $login_type = filter_var($request->input('email'), FILTER_VALIDATE_EMAIL) ? 'email' : 'uname';

        $request->merge([
            $login_type => $request->input('email')
        ]);

        if (!Auth::attempt($request->only($login_type, 'password'))) {
            return response()->json([
                'message'   => 'Invalid email or password',
            ], 401);
        }

        $user = $request->user();
        $token = $user->createToken('sibby-secret')->plainTextToken;

        //fcm token
        $fcm = $fcm->updateOrCreate([
            'role'    => auth()->user()->role,
            'fcm'     => $request->fcm,
            'user'    => auth()->user()->id,
        ]);
        
        return response()->json([
            'user'      => $user,
            'token'     => $token,
        ], 200);
    }

    public function user(User $user)
    {
        $user = $user->where('id', Auth::user()->id)->first();

        return response()->json([
            'user'      => $user,
        ]);
    }

    public function profile(Request $request, User $user)
    {
        $request->validate([
            'profile'    => 'required'
        ]);

        $profile = $request->file('profile');
        $nprofile = time() . '.' . $profile->getClientOriginalExtension();
        $profile->move('images', $nprofile);

        $user = $user->find(Auth::user()->id);

        $user->update([
            'profile'        => $nprofile,
        ]);

        return response()->json([
            'message'   => 'Profile photo update is successful',
        ], 200);
    }

    public function logout(Request $request, Fcm $fcm)
    {
        $user = $request->user();
        $user->tokens()->delete();

        $fcm = $fcm->where('user', Auth::user()->id);
        $fcm->delete();

        return response()->json([
            'message' => 'Logout Successfully',
        ], 200);
    }

    public function forgot(Request $request)
    {
        $request->validate([
            'email' => 'required|email'
        ]);

        $status = Password::sendResetLink(
            $request->only('email')
        );

        if ($status == Password::RESET_LINK_SENT) {
            return [
                'message' => __($status)
            ];
        }

        throw ValidationException::withMessages([
            'email' => [trans($status)],
        ]);
    }

    public function update(Request $request, User $user)
    {
        $request->validate([
            'name'      => 'required',
            'uname'     => 'required',
            'email'     => 'required',
            'rs'        => 'required',
            'address'   => 'required',
            'hp'        => 'required',
        ]);

        $user = $user->find(Auth::user()->id);
        $user->update([
            'name'      => $request->name,
            'uname'     => $request->uname,
            'email'     => $request->email,
            'rs'        => $request->rs,
            'address'   => $request->address,
            'hp'        => $request->hp,
        ]);

        return response()->json([
            'message' => 'Account update successful'
        ], 200);
    }

    public function home(News $news,User $user)
    {
        $user = $user->where('id', Auth::user()->id)->first();
        $news = $news->take(3)->get();

        return response()->json([
            'user'      => $user,
            'news'      => $news,
        ]);
    }

    public function admin(Request $request, User $user)
    {
        $request->validate([
            'name'      => 'required',
            'uname'     => 'required',
            'email'     => 'required',
            'address'   => 'required',
            'hp'        => 'required',
        ]);

        $user = $user->find(Auth::user()->id);
        $user->update([
            'name'      => $request->name,
            'uname'     => $request->uname,
            'email'     => $request->email,
            'address'   => $request->address,
            'hp'        => $request->hp,
        ]);

        return response()->json([
            'message' => 'Account update successful'
        ], 200);
    }
}