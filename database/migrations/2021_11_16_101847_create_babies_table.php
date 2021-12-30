<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateBabiesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('babies', function (Blueprint $table) {
            $table->id('id_baby');
            $table->string('name', 100);
            $table->enum('gender', ['1', '2']);
            $table->string('dad', 50);
            $table->string('mom', 50);
            $table->text('skl');
            $table->text('ktpd');
            $table->text('ktpm');
            $table->text('kk');
            $table->text('bk');
            $table->text('pdf')->nullable();
            $table->enum('status', ['1', '2', '3']);
            $table->bigInteger('admin')->unsigned()->nullable();
            $table->bigInteger('user')->unsigned()->nullable();
            $table->timestamps();

            $table->foreign('admin')->references('id')->on('users')->onDelete('CASCADE');
            $table->foreign('user')->references('id')->on('users')->onDelete('CASCADE');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('babies');
    }
}
