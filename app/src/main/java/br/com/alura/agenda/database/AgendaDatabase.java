package br.com.alura.agenda.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.model.Aluno;

@Database(entities = {Aluno.class}, version = 3, exportSchema = false)
public abstract class AgendaDatabase extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "agenda.db";

    public abstract AlunoDAO getRoomAlunoDao();

    public static AgendaDatabase getInstance(Context context) {
        return Room
                .databaseBuilder(context, AgendaDatabase.class, NOME_BANCO_DE_DADOS)
                .allowMainThreadQueries()
                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("ALTER TABLE aluno ADD COLUMN sobrenome TEXT");
                    }
                }, new Migration(2, 3) {
                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase database) {
                        database.execSQL("CREATE TABLE IF NOT EXISTS `Aluno_novo` " +
                                "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                "`nome` TEXT, " +
                                "`telefone` TEXT, " +
                                "`email` TEXT)");
                        database.execSQL("INSERT INTO Aluno_novo (id, nome, telefone, email) SELECT id, nome, telefone, email FROM Aluno");
                        database.execSQL("DROP TABLE Aluno");
                        database.execSQL("ALTER TABLE Aluno_novo RENAME TO Aluno");
                    }
                })
                .build();
    }
}
