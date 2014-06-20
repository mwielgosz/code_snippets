public class RootCheckThread extends AsyncTask<Void, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Void... params) {
		/*
		 * Check root access.
		 */
		Process p;
		DataOutputStream os = null;
		BufferedReader br = null;
		boolean hasRootResult = false;

		try {
			p = Runtime.getRuntime().exec("su");
			String uuid = null;

			os = new DataOutputStream(p.getOutputStream());
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			/* Get UID from shell */
			os.writeBytes("id\n");
			os.flush();

			/* Read result of id command */
			uuid = br.readLine();
			os.writeBytes("exit\n");
			os.flush();

			try {
				p.waitFor();
				if (uuid != null && uuid.contains("uid=0"))
					hasRootResult = true;
			} catch (InterruptedException e) {
				hasRootResult = false;
			}
		} catch (IOException e) {
			hasRootResult = false;
            Log.e("Check ROOT Access", e.getMessage());
        } finally {
			try {
				if (br != null) br.close();
				if (os != null) os.close();
			} catch (IOException e) {
				Log.e("Check ROOT Access", e.getMessage());
			}
		}
		return hasRootResult;
	}
}
