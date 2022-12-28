import itertools
import io
import time

PATH_FASTA = "uniprot_sprot.fasta"
PATH_DB = "fasta.db"
ABC = "ABCDEFGHIJKLMNOPQRSTUVWYZX"
LEN = 2

map_with_all_word = {
    ''.join(key): value
    for value, key in enumerate(itertools.product(*[ABC] * LEN))
}

def enc_dig(n):
    return n.to_bytes(2, byteorder='big', signed=False)


def dec_dig(b):
    return int.from_bytes(b, byteorder='big', signed=False)


def dec_seq(byte_string):
    f = io.BytesIO(byte_string)
    name = f.read(dec_dig(f.read(2))).decode('utf-8')
    sequence = f.read(dec_dig(f.read(2))).decode('utf-8')
    yield name, sequence
    bcod = f.read(2)
    while bcod:
        cod = dec_dig(bcod)
        poss = tuple(
            dec_dig(f.read(2))
            for _ in range(dec_dig(f.read(2)))
        )
        yield cod, poss
        bcod = f.read(2)


def enc_seq_dec(sequence):
    out = [b''] * len(map_with_all_word)
    for i in range(len(sequence) - LEN + 1):
        out[map_with_all_word[sequence[i:i + LEN]]] += enc_dig(i)
    g = map_with_all_word
    return out


def parse_file(path_fasta, path_out):
    with open(path_fasta) as f, open(path_out, 'wb') as out:
        line = f.readline().strip()
        time_bref = time.time()
        while line:
            print(time.time() - time_bref)
            name = line[1:]
            buf = []
            line = f.readline().strip().upper()
            while not line.startswith('>') and line:
                buf.append(line)
                line = f.readline().strip().upper()

            sequence = ''.join(buf)
            bseq = enc_seq(name, sequence)
            out.write(len(bseq).to_bytes(3, byteorder='big', signed=False) + bseq)


def enc_seq(name, sequence):
    b_name = name.encode('utf-8')
    out = enc_dig(len(b_name)) + b_name

    b_sequence = sequence.encode('utf-8')
    out += enc_dig(len(b_sequence)) + b_sequence

    decomposition = enc_seq_dec(sequence)
    for cod, positions in enumerate(decomposition):
        if positions:
            out += enc_dig(cod) + enc_dig(len(positions) // 2) + positions

    return out

#сначала запусти меня, я создал базу данных
if __name__ == '__main__':

    parse_file(PATH_FASTA, PATH_DB)
    print("бд создана")
