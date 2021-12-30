<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Baby;
use App\Models\Fcm;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class BabyController extends Controller
{
    public function store(Request $request, Baby $baby, Fcm $fcm)
    {

        $token = $fcm->where('role', '2')->get();

        $request->validate([
            'name'      => 'required',
            'gender'    => 'required',
            'dad'       => 'required',
            'mom'       => 'required',
            'skl'       => 'required',
            'ktpm'      => 'required',
            'ktpd'      => 'required',
            'kk'        => 'required',
            'bk'        => 'required',
        ]);

        //skl
        $skl = $request->file('skl');
        $nskl = $skl->getClientOriginalName();
        $skl->move('images', $nskl);

        //ktpd
        $ktpd = $request->file('ktpd');
        $nktpd = $ktpd->getClientOriginalName();
        $ktpd->move('images', $nktpd);

        //ktpm
        $ktpm = $request->file('ktpm');
        $nktpm = $ktpm->getClientOriginalName();
        $ktpm->move('images', $nktpm);

        //kk
        $kk = $request->file('kk');
        $nkk = $kk->getClientOriginalName();
        $kk->move('images', $nkk);

        //bk
        $bk = $request->file('bk');
        $nbk = $bk->getClientOriginalName();
        $bk->move('images', $nbk);

        $baby->create([
            'name'      => $request->name,
            'gender'    => $request->gender,
            'dad'       => $request->dad,
            'mom'       => $request->mom,
            'skl'       => $nskl,
            'ktpd'      => $nktpd,
            'ktpm'      => $nktpm,
            'kk'        => $nkk,
            'bk'        => $nbk,
            'status'    => '1',
            'user'      => Auth::user()->id,
        ]);

        sendMultipleDevice("/topics/submissions", "Ada Pengajuan Baru", auth()->user()->rs . " Melakukan Pengajuan");

    }

    public function index(Request $request, Baby $baby)
    {
        $baby = $baby->where('user', Auth::user()->id)->with('user')->when($request->get('q'), function ($query) use ($request){
            $query->where('name', 'like', "%{$request->get('q')}%");
        })->orderBy('id_baby','desc')->paginate(50);

        return response()->json($baby, 200);
    }

    public function admin(Request $request, Baby $baby)
    {
        $baby = $baby->whereIn('status', [1])->with('user')->when($request->get('q'), function ($query) use ($request){
            $query->where('name', 'like', "%{$request->get('q')}%");
        })->orderBy('id_baby','desc')->paginate(50);

        return response()->json($baby, 200);
    }

    public function update(Request $request, Baby $baby, Fcm $fcm, $id)
    {
        $token = $fcm->where('role', '2')->get();

        $request->validate([
            'name'      => 'required',
            'gender'    => 'required',
            'dad'       => 'required',
            'mom'       => 'required',
            'skl'       => 'required',
            'ktpm'      => 'required',
            'ktpd'      => 'required',
            'kk'        => 'required',
            'bk'        => 'required',
        ]);

        //skl
        $skl = $request->file('skl');
        $nskl = $skl->getClientOriginalName();
        $skl->move('images', $nskl);

        //ktpd
        $ktpd = $request->file('ktpd');
        $nktpd = $ktpd->getClientOriginalName();
        $ktpd->move('images', $nktpd);

        //ktpm
        $ktpm = $request->file('ktpm');
        $nktpm = $ktpm->getClientOriginalName();
        $ktpm->move('images', $nktpm);

        //kk
        $kk = $request->file('kk');
        $nkk = $kk->getClientOriginalName();
        $kk->move('images', $nkk);

        //bk
        $bk = $request->file('bk');
        $nbk = $bk->getClientOriginalName();
        $bk->move('images', $nbk);

        $baby = $baby->find($id);
        $baby->update([
            'name'      => $request->name,
            'gender'    => $request->gender,
            'dad'       => $request->dad,
            'mom'       => $request->mom,
            'skl'       => $nskl,
            'ktpd'      => $nktpd,
            'ktpm'      => $nktpm,
            'kk'        => $nkk,
            'bk'        => $nbk,
            'status'    => '1',
            'user'      => Auth::user()->id,
        ]);

        sendMultipleDevice("/topics/submissions", "Perbaikan Berkas", auth()->user()->rs . " Melakukan Sudah Melakukan Perbaikan Berkas");

    }

    public function status(Request $request, Baby $baby){
        $baby = $baby->where('user', Auth::user()->id)->with('user')->when($request->get('q'), function ($query) use ($request){
            $query->where('status', 'like', "%{$request->get('q')}%");
        })->orderBy('id_baby','desc')->paginate(50);

        return response()->json($baby, 200);
    }

    public function riject(Request $request, Baby $baby, Fcm $fcm, $id){

        $request->validate([
            'user'   => 'required',
            'baby'   => 'required',
            'status' => 'required'
        ]);

        $fcm = $fcm->where('user', $request->user)->value('fcm');

        $baby = $baby->find($id);
        $baby->update([
            'status' => $request->status
        ]);

        sendNotification($fcm, "Berkas Bayi " . $request->baby . " Ditolak", "Silahkan perbaiki berkas yang telah diajukan");

    }

    public function babies(Request $request, Baby $baby){
        $baby = $baby->with('user')->when($request->get('q'), function ($query) use ($request){
            $query->where('status', 'like', "%{$request->get('q')}%");
        })->orderBy('id_baby','desc')->paginate(50);

        return response()->json($baby, 200);
    }

    public function pdf(Request $request, Baby $baby, Fcm $fcm, $id){

        $request->validate([
            'user'   => 'required',
            'baby'   => 'required',
            'pdf'    => 'required',
            'status' => 'required'
        ]);

        $fcm = $fcm->where('user', $request->user)->value('fcm');

        //pdf
        $pdf = $request->file('pdf');
        $npdf = time() . '.' .  $pdf->getClientOriginalExtension();
        $pdf->move('drafts', $npdf);

        $baby = $baby->find($id);
        $baby->update([
            'pdf'    => $npdf,
            'status' => $request->status
        ]);

        sendNotification($fcm, "Yeay ! Berkas Bayi " . $request->baby . " Diterima", "Silahkan download draft kartu keluarga bayi " . $request->baby);

    }

}